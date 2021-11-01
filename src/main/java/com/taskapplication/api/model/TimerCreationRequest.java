package com.taskapplication.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimerCreationRequest {
    private short workCycleInMinutes;
    private short breakCycleInMinutes;
}
