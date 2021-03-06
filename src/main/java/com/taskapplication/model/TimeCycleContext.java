package com.taskapplication.model;

import java.util.HashMap;

import static com.taskapplication.model.TimeCycleStatusType.STARTED;
import static com.taskapplication.model.TimeCycleStatusType.CREATED;
import static com.taskapplication.model.TimeCycleStatusType.CANCELLED;
import static com.taskapplication.model.TimeCycleStatusType.FINISHED;
import static com.taskapplication.model.TimeCycleStatusType.PAUSED;
import static com.taskapplication.model.TimeCycleState.PassedTime;

public class TimeCycleContext {

    private static final HashMap<TimeCycleStatusType, TimeCycleState> stateMap = new HashMap<>();

    static {
        stateMap.put(STARTED, new StartedTimeCycleState());
        stateMap.put(CREATED, new CreatedTimeCycleState());
        stateMap.put(CANCELLED, new CancelledTimeCycleState());
        stateMap.put(FINISHED, new FinishedTimeCycleState());
        stateMap.put(PAUSED, new PausedTimeCycleState());
    }

    private TimeCycleState state;

    private final TimeCycle timeCycle;

    public TimeCycleContext(TimeCycle timeCycle) {
        if (timeCycle == null)
            throw new TimeCycleState.TimeCycleStateException("timeCycle should be defined.");
        this.timeCycle = timeCycle;
        this.state = stateMap.get(timeCycle.getStatus());
        if (state == null)
            throw new TimeCycleState.TimeCycleStateException("timeCycle state should be defined.");
    }

    public TimeCycle getTimeCycle() {
        return timeCycle;
    }

    public void setState(TimeCycleState state) {
        this.state = state;
    }

    public void start() {
        state.start(this);
    }

    public PassedTime pause(long passedWorkTimeInSeconds) {
        return state.pause(this, passedWorkTimeInSeconds);
    }

    public void resume() {
        state.resume(this);
    }

    public PassedTime cancel(long passedWorkTimeInSeconds, long passedBreakTimeInSeconds) {
        return state.cancel(this, passedWorkTimeInSeconds, passedBreakTimeInSeconds);
    }

    public PassedTime finish() {
        return state.finish(this);
    }
}
