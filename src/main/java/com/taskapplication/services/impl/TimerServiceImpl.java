package com.taskapplication.services.impl;

import com.taskapplication.api.models.TimerCreationRequest;
import com.taskapplication.api.models.TimerCycleAdditionRequest;
import com.taskapplication.models.TimeCycle;
import com.taskapplication.models.TimeCycleContext;
import com.taskapplication.models.TimeCycleState;
import com.taskapplication.models.TimeSpent;
import com.taskapplication.models.Timer;
import com.taskapplication.repositories.TimerRepository;
import com.taskapplication.services.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;
import static com.taskapplication.models.TimeCycleState.PassedTime;

@Service
public class TimerServiceImpl implements TimerService {

    private final TimerRepository timerRepository;

    @Autowired
    public TimerServiceImpl(TimerRepository timerRepository) {
        this.timerRepository = timerRepository;
    }

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
    public TimeCycle getActiveTimeCycle(long timerId) {
        return getActiveTimeCycle(findById(timerId));
    }

    public TimeCycle getActiveTimeCycle(Timer timer) {
        final List<TimeCycle> timeCycles = timer.getTimeCycles();
        return timeCycles.get(timeCycles.size() - 1);
    }

    @Override
    public Timer start(long timerId) {
        final Timer timer = findById(timerId);
        final TimeCycleContext timeCycleContext = new TimeCycleContext(getActiveTimeCycle(timer));
        timeCycleContext.start();
        return timerRepository.save(timer);
    }

    @Override
    public Timer resume(long timerId) {
        final Timer timer = findById(timerId);
        final TimeCycleContext timeCycleContext = new TimeCycleContext(getActiveTimeCycle(timer));
        timeCycleContext.resume();
        return timerRepository.save(timer);
    }

    @Override
    public Timer pause(long timerId, long passedWorkTimeInSeconds) {
        final Timer timer = findById(timerId);
        final TimeCycleContext timeCycleContext = new TimeCycleContext(getActiveTimeCycle(timer));
        final TimeCycleState.PassedTime passedTime = timeCycleContext.pause(passedWorkTimeInSeconds);
        return timerRepository.save(getPassedTimeIncludedTimer(timer, passedTime));
    }

    @Override
    public Timer cancel(long timerId, long passedWorkTimeInSeconds, long passedBreakTimeInSeconds) {
        final Timer timer = findById(timerId);
        final TimeCycleContext timeCycleContext = new TimeCycleContext(getActiveTimeCycle(timer));
        final PassedTime passedTime = timeCycleContext.cancel(passedWorkTimeInSeconds, passedBreakTimeInSeconds);
        return timerRepository.save(getPassedTimeIncludedTimer(timer, passedTime));
    }

    @Override
    public Timer finish(long timerId) {
        final Timer timer = findById(timerId);
        final TimeCycleContext timeCycleContext = new TimeCycleContext(getActiveTimeCycle(timer));
        final PassedTime passedTime = timeCycleContext.finish();
        return timerRepository.save(getPassedTimeIncludedTimer(timer, passedTime));
    }

    private Timer getTimerById(TimerCycleAdditionRequest timerCycleAdditionRequest) {
        checkAdditionRequest(timerCycleAdditionRequest);
        return findById(timerCycleAdditionRequest.getTimerId());
    }

    private void checkAdditionRequest(TimerCycleAdditionRequest timerCycleAdditionRequest) {
        if (isNull(timerCycleAdditionRequest))
            throw new RuntimeException();
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
}
