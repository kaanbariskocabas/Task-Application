package com.taskapplication.repositories;

import com.taskapplication.models.TimeCycle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeCycleRepository extends JpaRepository<TimeCycle, Long> {
}
