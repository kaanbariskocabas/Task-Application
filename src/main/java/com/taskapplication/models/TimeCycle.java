package com.taskapplication.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class TimeCycle {

    @Id
    @GeneratedValue
    @Column(name = "cycle_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timer_id", nullable = false)
    private Timer timer;

    @Column(nullable = false)
    private LocalDateTime startedAt = LocalDateTime.now();

    private LocalDateTime endedAt;

    /* Default value will be 25 */
    private short workCycleInMinutes = 25;

    /* Default value will be 5 */
    private short restCycleInMinutes = 5;

    private boolean finished;

    private boolean paused;

    private LocalDateTime pausedAt;

    private long remainingTimeInSeconds;
}
