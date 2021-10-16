package com.taskapplication.api.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Getter
public class ApiResponse {

    private final String id = UUID.randomUUID().toString();

    private final String message;

    private final HttpStatus status;

    private final boolean success;

    public ApiResponse(Exception e, HttpStatus status) {
        message = e.getMessage();
        this.status = status;
        success = false;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", status=" + status +
                ", success=" + success +
                '}';
    }
}
