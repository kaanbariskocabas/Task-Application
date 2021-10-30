package com.taskapplication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskapplication.exception.BaseException;
import com.taskapplication.model.Task;
import com.taskapplication.model.Timer;
import com.taskapplication.repositories.TaskRepository;
import com.taskapplication.repositories.TimerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.taskapplication.util.ArrayUtil.convertArrayToList;
import static com.taskapplication.util.JpaRepositoryUtil.isJpaRepositoryEmpty;
import static java.util.Objects.isNull;

@Configuration
@Slf4j
public class DataConfiguration implements CommandLineRunner {

    private static String taskDataJsonFileName;
    private static String timerDataJsonFileName;

    private final TaskRepository taskRepository;

    private final TimerRepository timerRepository;

    public DataConfiguration(Environment environment, TaskRepository taskRepository, TimerRepository timerRepository) {
        this.taskRepository = taskRepository;
        this.timerRepository = timerRepository;
        setPropertyFields(environment);
    }

    private static void setPropertyFields(Environment environment) {
        taskDataJsonFileName = environment.getProperty("TASK_DATA_JSON_FILE");
        timerDataJsonFileName = environment.getProperty("TIMER_DATA_JSON_FILE");
    }

    @Override
    public void run(String... args) throws Exception {
        loadIndividualTimers();
        loadTasks();
    }

    private void loadIndividualTimers() throws IOException {
        final boolean isTimerRepoEmpty = isJpaRepositoryEmpty(timerRepository);
        if (isTimerRepoEmpty) {
            final List<Timer> timers = getTimers();
            timerRepository.saveAll(timers);
        }
    }

    private void loadTasks() throws IOException {
        final boolean isTaskRepoEmpty = isJpaRepositoryEmpty(taskRepository);
        if (isTaskRepoEmpty)
            taskRepository.saveAll(getTasks());
    }

    private List<Timer> getTimers() throws IOException {
        return getMappedList(timerDataJsonFileName, Timer[].class);
    }

    private List<Task> getTasks() throws IOException {
        return getMappedList(taskDataJsonFileName, Task[].class);
    }

    private <T> List<T> getMappedList(String timerDataJsonFile, Class<T[]> valueType) throws IOException {
        return convertArrayToList(getMappedObjects(timerDataJsonFile, valueType));
    }

    private <T> T[] getMappedObjects(String fileName, Class<T[]> valueType) throws IOException {
        if (isNull(valueType))
            throw new DataLoadException("Please specify a class type for mapping");

        return new ObjectMapper().readValue(getDataFileAsStream(fileName), valueType);
    }

    private InputStream getDataFileAsStream(String fileName) {
        return DataConfiguration.class.getClassLoader().getResourceAsStream(fileName);
    }

    private static class DataLoadException extends BaseException {
        private DataLoadException(String message) {
            super(message);
        }
    }
}
