package com.taskapplication.service;

import com.taskapplication.api.model.TaskCreationRequest;
import com.taskapplication.api.model.TaskUpdateRequest;
import com.taskapplication.model.Task;

import java.util.List;

public interface TaskService extends BaseService {
    Task create(TaskCreationRequest taskCreationRequest);
    void delete(Long taskId);
    Task update(TaskUpdateRequest taskUpdateRequest);
    List<Task> filter();
}
