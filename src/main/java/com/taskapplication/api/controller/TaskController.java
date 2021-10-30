package com.taskapplication.api.controller;

import com.taskapplication.api.model.TaskCreationRequest;
import com.taskapplication.api.model.TaskUpdateRequest;
import com.taskapplication.model.Task;
import com.taskapplication.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{taskId}")
    public Task findById(@PathVariable("taskId") long taskId) {
        return taskService.findById(taskId);
    }

    @PostMapping
    public Task create(TaskCreationRequest taskCreationRequest) {
        return taskService.create(taskCreationRequest);
    }

    @PutMapping
    public Task create(TaskUpdateRequest taskUpdateRequest) {
        return taskService.update(taskUpdateRequest);
    }

    @DeleteMapping("/{taskId}")
    public void delete(@PathVariable("taskId") long taskId) {
        taskService.delete(taskId);
    }
}