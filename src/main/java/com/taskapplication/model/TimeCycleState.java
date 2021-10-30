package com.taskapplication.model;

import com.taskapplication.exception.BaseException;

import java.time.OffsetDateTime;

import static com.taskapplication.model.TimeCycleStatusType.FINISHED;
import static com.taskapplication.model.TimeCycleStatusType.STARTED;
import static com.taskapplication.model.TimeCycleStatusType.CANCELLED;
import static com.taskapplication.model.TimeCycleStatusType.PAUSED;

public abstract class TimeCycleState {

    public static class PassedTime {
        private final long passedWorkTimeInSeconds;
        private final long passedBreakTimeInSeconds;

        public PassedTime(long passedWorkTimeInSeconds, long passedBreakTimeInSeconds) {
            this.passedWorkTimeInSeconds = passedWorkTimeInSeconds;
            this.passedBreakTimeInSeconds = passedBreakTimeInSeconds;
        }

        public long getPassedBreakTimeInSeconds() {
            return passedBreakTimeInSeconds;
        }

        public long getPassedWorkTimeInSeconds() {
            return passedWorkTimeInSeconds;
        }
    }

    public static final class TimeCycleStateException extends BaseException {

        TimeCycleStateException(String message) {
            super(message);
        }
    }

    void start(TimeCycleContext context) {
        throw new TimeCycleStateException(getExceptionMessage(context, "START"));
    }

    PassedTime pause(TimeCycleContext context, long passedWorkTimeInSeconds) {
        throw new TimeCycleStateException(getExceptionMessage(context, "PAUSE"));
    }

    void resume(TimeCycleContext context) {
        throw new TimeCycleStateException(getExceptionMessage(context, "RESUME"));
    }

    PassedTime cancel(TimeCycleContext context, long passedWorkTimeInSeconds, long passedBreakTimeInSeconds) {
        throw new TimeCycleStateException(getExceptionMessage(context, "CANCEL"));
    }

    PassedTime finish(TimeCycleContext context) {
        throw new TimeCycleStateException(getExceptionMessage(context, "FINISH"));
    }

    private String getExceptionMessage(TimeCycleContext context, String expectedState) {
        return String.format("This cycle is in %s state and %s operation could not be continued...", getStatus(context),
                expectedState);
    }

    private String getStatus(TimeCycleContext context) {
        return getTimeCycle(context).getStatus().name();
    }

    protected TimeCycle getTimeCycle(TimeCycleContext context) {
        return context.getTimeCycle();
    }

    protected void checkWorkTime(TimeCycleContext context, long workTime) {
        final TimeCycle timeCycle = getTimeCycle(context);
        final long workTimeLeft = getWorkTimeLeft(timeCycle);
        if (workTime <= 0 || workTime > workTimeLeft)
            throw new TimeCycleStateException(
                    String.format("Work time should be between 1 and %d seconds", workTimeLeft));
    }

    protected long getWorkTimeLeft(TimeCycle timeCycle) {
        final long totalWorkTimeInSeconds = timeCycle.getWorkCycleInMinutes() * 60;
        final long workTimeUntilNowInSeconds = timeCycle.getTotalWorkInSeconds();
        return totalWorkTimeInSeconds - workTimeUntilNowInSeconds;
    }

    protected void checkBreakTime(TimeCycleContext context, long breakTime) {
        final int totalBreakTimeInSeconds = getTimeCycle(context).getBreakCycleInMinutes() * 60;
        if (breakTime < 0 || breakTime > totalBreakTimeInSeconds)
            throw new TimeCycleStateException(
                    String.format("Break time should be between 0 and %d seconds", totalBreakTimeInSeconds));
    }

    protected PassedTime getPassedTime(long passedWorkTimeInSeconds, long passedBreakTimeInSeconds) {
        return new PassedTime(passedWorkTimeInSeconds, passedBreakTimeInSeconds);
    }

    protected PassedTime setCancelledState(TimeCycleContext context, long passedWorkTimeInSeconds, long passedBreakTimeInSeconds) {
        checkWorkTime(context, passedWorkTimeInSeconds);
        checkBreakTime(context, passedBreakTimeInSeconds);
        final TimeCycle timeCycle = getTimeCycle(context);
        timeCycle.setCancelledAt(OffsetDateTime.now());
        timeCycle.setStatus(CANCELLED);
        context.setState(new CancelledTimeCycleState());
        return getPassedTime(passedWorkTimeInSeconds, passedBreakTimeInSeconds);
    }

    protected void setStartedState(TimeCycleContext context) {
        final TimeCycle timeCycle = getTimeCycle(context);
        timeCycle.setStartedAt(OffsetDateTime.now());
        timeCycle.setStatus(STARTED);
        context.setState(new StartedTimeCycleState());
    }
}

final class CreatedTimeCycleState extends TimeCycleState {

    @Override
    void start(TimeCycleContext context) {
        setStartedState(context);
    }

    @Override
    PassedTime cancel(TimeCycleContext context, long passedWorkTimeInSeconds, long passedBreakTimeInSeconds) {
        return setCancelledState(context, passedWorkTimeInSeconds, passedBreakTimeInSeconds);
    }
}

final class StartedTimeCycleState extends TimeCycleState {
    @Override
    PassedTime pause(TimeCycleContext context, long passedWorkTimeInSeconds) {
        checkWorkTime(context, passedWorkTimeInSeconds);
        final TimeCycle timeCycle = getTimeCycle(context);
        timeCycle.setPausedAt(OffsetDateTime.now());
        timeCycle.setStatus(PAUSED);
        context.setState(new PausedTimeCycleState());
        return getPassedTime(passedWorkTimeInSeconds, 0);
    }

    @Override
    PassedTime cancel(TimeCycleContext context, long passedWorkTimeInSeconds, long passedBreakTimeInSeconds) {
        return setCancelledState(context, passedWorkTimeInSeconds, passedBreakTimeInSeconds);
    }

    @Override
    PassedTime finish(TimeCycleContext context) {
        final TimeCycle timeCycle = getTimeCycle(context);
        timeCycle.setFinishedAt(OffsetDateTime.now());
        timeCycle.setStatus(FINISHED);
        context.setState(new FinishedTimeCycleState());
        return getPassedTime(getWorkTimeLeft(timeCycle), timeCycle.getBreakCycleInMinutes() * 60);
    }
}

final class CancelledTimeCycleState extends TimeCycleState {
}

final class FinishedTimeCycleState extends TimeCycleState {
}

final class PausedTimeCycleState extends TimeCycleState {
    @Override
    void resume(TimeCycleContext context) {
        setStartedState(context);
    }

    @Override
    PassedTime cancel(TimeCycleContext context, long passedWorkTimeInSeconds, long passedBreakTimeInSeconds) {
        return setCancelledState(context, passedWorkTimeInSeconds, passedBreakTimeInSeconds);
    }
}