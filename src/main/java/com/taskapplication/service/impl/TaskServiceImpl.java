package com.taskapplication.service.impl;

import com.taskapplication.api.model.TaskCreationRequest;
import com.taskapplication.api.model.TaskUpdateRequest;
import com.taskapplication.exception.BaseException;
import com.taskapplication.model.Task;
import com.taskapplication.repositories.TaskRepository;
import com.taskapplication.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.taskapplication.util.StringUtil.isBlank;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task create(TaskCreationRequest taskCreationRequest) {
        return taskRepository.save(getNewTask(taskCreationRequest));
    }

    @Override
    public void delete(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public Task update(TaskUpdateRequest taskUpdateRequest) {
        return taskRepository.save(getUpdatedTask(taskUpdateRequest));
    }

    @Override
    public List<Task> filter() {
        return null;
    }

    @Override
    public Task findById(long taskId) {
        return taskRepository.findById(getId(taskId))
                .orElseThrow(() -> new TaskValidationException("Specified task is not found."));
    }

    private static Task getNewTask(TaskCreationRequest taskCreationRequest) {
        checkTaskCreateRequest(taskCreationRequest);
        final Task task = new Task();
        task.setTitle(taskCreationRequest.getTitle());
        task.setContent(taskCreationRequest.getContent());
        return task;
    }

    private Task getUpdatedTask(TaskUpdateRequest taskUpdateRequest) {
        checkTaskUpdateRequest(taskUpdateRequest);
        final Task task = findById(taskUpdateRequest.getTaskId());
        task.setTitle(taskUpdateRequest.getTitle());
        task.setContent(taskUpdateRequest.getContent());
        return task;
    }

    private static void checkTaskUpdateRequest(TaskUpdateRequest taskUpdateRequest) {
        if (taskUpdateRequest == null || taskUpdateRequest.getTaskId() < 1)
            throw new TaskValidationException("Task id must be provided.");
    }

    private static void checkTaskCreateRequest(TaskCreationRequest taskCreationRequest) {
        if (taskCreationRequest == null || isBlank(taskCreationRequest.getTitle()))
            throw new TaskValidationException("Title must be provided.");
    }

    public static class TaskValidationException extends BaseException {
        private TaskValidationException(String message) {
            super(message);
        }
    }
}
