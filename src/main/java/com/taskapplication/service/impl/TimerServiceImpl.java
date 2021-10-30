package com.taskapplication.service.impl;

import com.taskapplication.api.model.TimerCreationRequest;
import com.taskapplication.api.model.TimerCycleAdditionRequest;
import com.taskapplication.exception.BaseException;
import com.taskapplication.model.TimeCycle;
import com.taskapplication.model.TimeCycleContext;
import com.taskapplication.model.TimeCycleState;
import com.taskapplication.model.TimeSpent;
import com.taskapplication.model.Timer;
import com.taskapplication.repositories.TimerRepository;
import com.taskapplication.service.TimerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;
import static com.taskapplication.model.TimeCycleState.PassedTime;

@Service
@RequiredArgsConstructor
public class TimerServiceImpl implements TimerService {

    private final TimerRepository timerRepository;

    @Override
    public Timer findById(long timerId) {
        return timerRepository.findById(getId(timerId)).orElseThrow(RuntimeException::new);
    }

    @Override
    public Timer create(TimerCreationRequest timerCreationRequest) {
        return timerRepository.save(getNewTimer(timerCreationRequest));
    }

    @Override
    public void delete(long timerId) {
        timerRepository.deleteById(getId(timerId));
    }

    @Override
    public Timer addTimeCycle(TimerCycleAdditionRequest timerCycleAdditionRequest) {
        Timer timer = getTimerById(timerCycleAdditionRequest);
        timer.addTimeCycle(getTimeCycle(timerCycleAdditionRequest));
        return timerRepository.save(timer);
    }

    @Override
    public TimeCycle getCurrentTimeCycle(long timerId) {
        return getCurrentTimeCycle(findById(timerId));
    }

    @Override
    public Timer start(long timerId) {
        final Timer timer = findById(timerId);
        final TimeCycleContext timeCycleContext = new TimeCycleContext(getCurrentTimeCycle(timer));
        timeCycleContext.start();
        return timerRepository.save(timer);
    }

    @Override
    public Timer resume(long timerId) {
        final Timer timer = findById(timerId);
        final TimeCycleContext timeCycleContext = new TimeCycleContext(getCurrentTimeCycle(timer));
        timeCycleContext.resume();
        return timerRepository.save(timer);
    }

    @Override
    public Timer pause(long timerId, long passedWorkTimeInSeconds) {
        final Timer timer = findById(timerId);
        final TimeCycleContext timeCycleContext = new TimeCycleContext(getCurrentTimeCycle(timer));
        final TimeCycleState.PassedTime passedTime = timeCycleContext.pause(passedWorkTimeInSeconds);
        return timerRepository.save(getPassedTimeIncludedTimer(timer, passedTime));
    }

    @Override
    public Timer cancel(long timerId, long passedWorkTimeInSeconds, long passedBreakTimeInSeconds) {
        final Timer timer = findById(timerId);
        final TimeCycleContext timeCycleContext = new TimeCycleContext(getCurrentTimeCycle(timer));
        final PassedTime passedTime = timeCycleContext.cancel(passedWorkTimeInSeconds, passedBreakTimeInSeconds);
        return timerRepository.save(getPassedTimeIncludedTimer(timer, passedTime));
    }

    @Override
    public Timer finish(long timerId) {
        final Timer timer = findById(timerId);
        final TimeCycleContext timeCycleContext = new TimeCycleContext(getCurrentTimeCycle(timer));
        final PassedTime passedTime = timeCycleContext.finish();
        return timerRepository.save(getPassedTimeIncludedTimer(timer, passedTime));
    }

    private TimeCycle getCurrentTimeCycle(Timer timer) {
        final List<TimeCycle> timeCycles = timer.getTimeCycles();
        return timeCycles.get(timeCycles.size() - 1);
    }

    private Timer getTimerById(TimerCycleAdditionRequest timerCycleAdditionRequest) {
        checkAdditionRequest(timerCycleAdditionRequest);
        return findById(timerCycleAdditionRequest.getTimerId());
    }

    private void checkAdditionRequest(TimerCycleAdditionRequest timerCycleAdditionRequest) {
        if (isNull(timerCycleAdditionRequest))
            throw new TimerValidationException("Timer addition request should be defined.");
    }

    private Timer getPassedTimeIncludedTimer(Timer timer, PassedTime passedTime) {
        final TimeSpent totalTimeSpent = timer.getTotalTimeSpent();
        totalTimeSpent.addTime(passedTime.getPassedWorkTimeInSeconds(), passedTime.getPassedBreakTimeInSeconds());
        timer.setTotalTimeSpent(totalTimeSpent);
        return timer;
    }

    private Timer getNewTimer(TimerCreationRequest timerCreationRequest) {
        return new Timer(getTimeCycle(timerCreationRequest));
    }

    private TimeCycle getTimeCycle(TimerCreationRequest timerCreationRequest) {
        if (isNull(timerCreationRequest))
            return new TimeCycle();
        return new TimeCycle(timerCreationRequest.getWorkCycleInMinutes(), timerCreationRequest.getRestCycleInMinutes());
    }

    public static class TimerValidationException extends BaseException {
        private TimerValidationException(String message) {
            super(message);
        }
    }
}
