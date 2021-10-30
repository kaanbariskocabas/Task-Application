package com.taskapplication.repositories;

import com.taskapplication.model.TimeCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeCycleRepository extends JpaRepository<TimeCycle, Long> {
}
