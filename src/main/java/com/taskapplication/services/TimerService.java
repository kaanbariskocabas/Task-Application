package com.taskapplication.services;

import com.taskapplication.api.models.TimerCreationRequest;
import com.taskapplication.api.models.TimerCycleAdditionRequest;
import com.taskapplication.models.TimeCycle;
import com.taskapplication.models.Timer;

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
