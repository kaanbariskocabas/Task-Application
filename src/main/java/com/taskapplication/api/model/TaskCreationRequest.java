package com.taskapplication.api.model;

import lombok.Getter;

@Getter
public class TaskCreationRequest {
    private String title;
    private String content;
}
