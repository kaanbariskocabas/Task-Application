package com.taskapplication.models;

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
    private long id;

    private long totalTimeInSeconds;

    private long breakTimeInSeconds;

    private long workTimeInSeconds;
}
