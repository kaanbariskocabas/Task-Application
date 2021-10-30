package com.taskapplication.api.controller;

import com.taskapplication.model.Timer;
import com.taskapplication.repositories.TimerRepository;
import com.taskapplication.service.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/timer")
public class TimerController {

    @Autowired
    private TimerRepository repository;

    @Autowired
    private TimerService timerService;

    @GetMapping()
    public List<Timer> findAll() {
        return repository.findAll();
    }

    @PostMapping("/start/{timerId}")
    public Timer start(@PathVariable("timerId") long timerId) {
        return timerService.start(timerId);
    }
}
