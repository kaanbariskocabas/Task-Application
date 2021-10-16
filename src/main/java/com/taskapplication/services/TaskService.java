package com.taskapplication.services;

import com.taskapplication.api.models.TaskCreationRequest;
import com.taskapplication.api.models.TaskUpdateRequest;
import com.taskapplication.models.Task;

import java.util.List;

public interface TaskService extends BaseService {
    Task create(TaskCreationRequest taskCreationRequest);
    void delete(Long taskId);
    Task update(TaskUpdateRequest taskUpdateRequest);
    List<Task> filter();
}
