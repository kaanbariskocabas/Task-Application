package com.taskapplication.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class TaskUpdateRequest extends TaskCreationRequest {
    private long taskId;
}
