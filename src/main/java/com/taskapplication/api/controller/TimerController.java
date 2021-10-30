package com.taskapplication.api.controller;

import com.taskapplication.api.model.TimerCreationRequest;
import com.taskapplication.api.model.TimerCycleAdditionRequest;
import com.taskapplication.model.TimeCycle;
import com.taskapplication.model.Timer;
import com.taskapplication.repositories.TimerRepository;
import com.taskapplication.service.TimerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/timer")
@RequiredArgsConstructor
public class TimerController {

    private final TimerService timerService;

    @PutMapping("/start/{timerId}")
    public Timer start(@PathVariable("timerId") long timerId) {
        return timerService.start(timerId);
    }

    @PutMapping("/finish/{timerId}")
    public Timer finish(@PathVariable("timerId") long timerId) {
        return timerService.finish(timerId);
    }

    @PutMapping("/resume/{timerId}")
    public Timer resume(@PathVariable("timerId") long timerId) {
        return timerService.resume(timerId);
    }

    @PutMapping("/pause/{timerId}")
    public Timer pause(@PathVariable("timerId") long timerId,
                       @RequestParam("passedWorkTimeInSeconds") long passedWorkTimeInSeconds) {
        return timerService.pause(timerId, passedWorkTimeInSeconds);
    }

    @PutMapping("/cancel/{timerId}")
    public Timer cancel(@PathVariable("timerId") long timerId,
                        @RequestParam("passedWorkTimeInSeconds") long passedWorkTimeInSeconds,
                        @RequestParam("passedBreakTimeInSeconds") long passedBreakTimeInSeconds) {
        return timerService.cancel(timerId, passedWorkTimeInSeconds, passedBreakTimeInSeconds);
    }

    @PostMapping
    public Timer create(@RequestBody TimerCreationRequest timerCreationRequest) {
        return timerService.create(timerCreationRequest);
    }

    @DeleteMapping("/{timerId}")
    public void delete(@PathVariable("timerId") long timerId) {
        timerService.delete(timerId);
    }

    @PostMapping("/timerCycle")
    public Timer addTimeCycle(@RequestBody TimerCycleAdditionRequest timerCycleAdditionRequest) {
        return timerService.addTimeCycle(timerCycleAdditionRequest);
    }

    @GetMapping("/timerCycle/{timerId}")
    public TimeCycle getCurrentTimeCycle(@PathVariable("timerId") long timerId) {
        return timerService.getCurrentTimeCycle(timerId);
    }


}
