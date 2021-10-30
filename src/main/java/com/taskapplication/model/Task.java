package com.taskapplication.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private TaskStatusType status;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Timer timer;

    private boolean closed;

    private LocalDateTime closedAt;

}
