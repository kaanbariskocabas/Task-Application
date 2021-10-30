package com.taskapplication.service;

public interface BaseService {
    default long getId(long id) {
        if (id <= 0)
            throw new RuntimeException();
        return id;
    }
}
