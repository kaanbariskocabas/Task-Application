package com.taskapplication.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class TimeSpent {

    @Id
    @GeneratedValue
    private Long id;

    private long totalTimeInSeconds;

    private long breakTimeInSeconds;

    private long workTimeInSeconds;

    public void addTime(long workSeconds, long breakSeconds) {
        workTimeInSeconds += workSeconds;
        breakTimeInSeconds += breakSeconds;
        totalTimeInSeconds = workTimeInSeconds + breakTimeInSeconds;
    }
}
