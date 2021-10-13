package com.taskapplication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskapplication.models.Task;
import com.taskapplication.models.TimeCycle;
import com.taskapplication.models.Timer;
import com.taskapplication.repositories.TaskRepository;
import com.taskapplication.repositories.TimeCycleRepository;
import com.taskapplication.repositories.TimerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.taskapplication.util.ArrayUtil.convertArrayToList;
import static com.taskapplication.util.JpaRepositoryUtil.isJpaRepositoryEmpty;

@Configuration
@Slf4j
public class DataConfiguration implements CommandLineRunner {

    private static String TASK_DATA_JSON_FILE;
    private static String TIMER_DATA_JSON_FILE;

    private final TaskRepository taskRepository;

    private final TimerRepository timerRepository;

    private final TimeCycleRepository timeCycleRepository;

    @Autowired
    public DataConfiguration(Environment environment, TaskRepository taskRepository, TimerRepository timerRepository, TimeCycleRepository timeCycleRepository) {
        this.taskRepository = taskRepository;
        this.timerRepository = timerRepository;
        this.timeCycleRepository = timeCycleRepository;
        TASK_DATA_JSON_FILE = environment.getProperty("TASK_DATA_JSON_FILE");
        TIMER_DATA_JSON_FILE = environment.getProperty("TIMER_DATA_JSON_FILE");
    }

    @Override
    public void run(String... args) throws Exception {
        addIndividualTimers();
        addTasks();
    }

    private void addIndividualTimers() throws IOException {
        final boolean isTimerRepoEmpty = isJpaRepositoryEmpty(timerRepository);
        if (isTimerRepoEmpty) {
            timerRepository.saveAll(getTimers());
        }
    }

    private void addTasks() throws IOException {
        final boolean isTaskRepoEmpty = isJpaRepositoryEmpty(taskRepository);
        if (isTaskRepoEmpty)
            taskRepository.saveAll(getTasks());
    }

    private List<Timer> getTimers() throws IOException {
        return getMappedList(TIMER_DATA_JSON_FILE, Timer[].class);
    }

    private List<Task> getTasks() throws IOException {
        return getMappedList(TASK_DATA_JSON_FILE, Task[].class);
    }

    private <T> List<T> getMappedList(String timerDataJsonFile, Class<T[]> valueType) throws IOException {
        final T[] mappedObjects = getMappedObjects(timerDataJsonFile, valueType);
        return convertArrayToList(mappedObjects);
    }

    private <T> T getMappedObjects(String fileName, Class<T> valueType) throws IOException {
        if (valueType != null) {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(getDataFile(fileName), valueType);

        }
        throw new RuntimeException("Please specify a class type");
    }

    private File getDataFile(String fileName) throws IOException {
        final URL resource = DataConfiguration.class.getClassLoader().getResource(fileName);
        if (resource == null || resource.getFile() == null)
            throw new IOException("File not found");
        return new File(resource.getFile());
    }
}
