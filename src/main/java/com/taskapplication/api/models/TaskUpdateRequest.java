package com.taskapplication.api.models;

import com.taskapplication.models.TaskStatusType;
import lombok.Getter;

@Getter
public final class TaskUpdateRequest extends TaskCreationRequest {
    private TaskStatusType status;
    private TimerCreationRequest timerRequest;
}
