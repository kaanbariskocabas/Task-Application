package com.taskapplication.api.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimerCycleAdditionRequest extends TimerCreationRequest {
    private long timerId;
}
