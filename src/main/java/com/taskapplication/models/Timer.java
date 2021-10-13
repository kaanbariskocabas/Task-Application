package com.taskapplication.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Timer {

    @Id
    @GeneratedValue
    private long id;

//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    @JoinColumn(name = "timer_id", nullable = false)
    @OneToMany(mappedBy = "timer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TimeCycle> timeCycles = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private TimeSpent totalTimeSpent;

    private void setTimeCycles(List<TimeCycle> timeCycles) {
        this.timeCycles = timeCycles;
        timeCycles.forEach(timeCycle -> timeCycle.setTimer(this));
    }

}
