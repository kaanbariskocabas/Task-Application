package com.taskapplication.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class TaskCreationRequest {
    private String title;
    private String content;
}
