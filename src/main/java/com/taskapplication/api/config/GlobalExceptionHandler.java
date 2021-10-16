package com.taskapplication.api.config;

import com.taskapplication.api.models.ApiResponse;
import com.taskapplication.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {BaseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse validationException(Exception e, WebRequest request) {
        log.error(e.getMessage());
        return new ApiResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse unknownException(Exception e, WebRequest request) {
        log.error(e.getMessage());
        return new ApiResponse(e, HttpStatus.BAD_REQUEST);
    }

}
