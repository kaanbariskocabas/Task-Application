package com.taskapplication.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TimeCycle {

    @Id
    @GeneratedValue
    @Column(name = "cycle_id")
    private Long id;

    private OffsetDateTime createdAt = OffsetDateTime.now();

    private OffsetDateTime startedAt;

    private OffsetDateTime cancelledAt;

    private OffsetDateTime finishedAt;

    private TimeCycleStatusType status = TimeCycleStatusType.CREATED;

    /* Default value will be 25 */
    @Column(nullable = false)
    private short workCycleInMinutes = 25;

    /* Default value will be 5 */
    @Column(nullable = false)
    private short breakCycleInMinutes = 5;

    private OffsetDateTime pausedAt;

    @Column(nullable = false)
    private long totalWorkInSeconds;

    public TimeCycle(short workCycleInMinutes, short breakCycleInMinutes) {
        if (workCycleInMinutes > 0)
            this.workCycleInMinutes = workCycleInMinutes;
        if (breakCycleInMinutes > -1)
            this.breakCycleInMinutes = breakCycleInMinutes;
    }

}
