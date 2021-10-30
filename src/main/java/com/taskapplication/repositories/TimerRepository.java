package com.taskapplication.repositories;

import com.taskapplication.model.Timer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimerRepository extends JpaRepository<Timer, Long> {
}
