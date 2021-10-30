package com.taskapplication.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreationRequest {
    private String title;
    private String content;
}
