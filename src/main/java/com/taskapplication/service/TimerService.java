package com.taskapplication.service;

import com.taskapplication.api.model.TimerCreationRequest;
import com.taskapplication.api.model.TimerCycleAdditionRequest;
import com.taskapplication.model.TimeCycle;
import com.taskapplication.model.Timer;

public interface TimerService extends BaseService {
    Timer findById(long timerId);
    Timer create(TimerCreationRequest timerCreationRequest);
    void delete(long timerId);
    Timer addTimeCycle(TimerCycleAdditionRequest timerCycleAdditionRequest);
    TimeCycle getActiveTimeCycle(long timerId);
    Timer start(long timerId);
    Timer resume(long timerId);
    Timer pause(long timerId, long passedWorkTimeInSeconds);
    Timer cancel(long timerId, long passedWorkTimeInSeconds, long passedBreakTimeInSeconds);
    Timer finish(long timerId);
}
