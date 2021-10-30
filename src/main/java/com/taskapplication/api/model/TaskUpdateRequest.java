package com.taskapplication.api.model;

import com.taskapplication.model.TaskStatusType;
import lombok.Getter;

@Getter
public final class TaskUpdateRequest extends TaskCreationRequest {
    private TaskStatusType status;
    private TimerCreationRequest timerRequest;
}
