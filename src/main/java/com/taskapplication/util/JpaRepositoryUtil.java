package com.taskapplication.util;

import org.springframework.data.jpa.repository.JpaRepository;

public final class JpaRepositoryUtil {

    private JpaRepositoryUtil() { }

    public static <T extends JpaRepository<?,?>> boolean isJpaRepositoryEmpty(T repository) {
        return repository.count() == 0;
    }
}
