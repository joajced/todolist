package com.github.joajced.todolist.service;

import com.github.joajced.todolist.model.Task;
import com.github.joajced.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {

        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getTasks() {

        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {

        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent()) return optionalTask.get();

        throw new RuntimeException("Task with id " + id + " does not exist.");
    }

    @Override
    public Task createTask(Task task) {

        return taskRepository.save(task);
    }

    @Override
    public Task patchTask(Long id, Map<String, Object> fields) {

        Optional<Task> needsPatch = taskRepository.findById(id);

        if (needsPatch.isPresent()) {

            fields.forEach((key, value) -> {
                switch (key) {
                    case "content":
                        needsPatch.get().setContent((String) value);
                        break;
                    case "done":
                        needsPatch.get().setDone((Boolean) value);
                        break;
                }
            });

            return taskRepository.save(needsPatch.get());
        }

        throw new RuntimeException("Task with id " + id + " does not exist.");
    }

    @Override
    public void deleteTask(Long id) {

        taskRepository.deleteById(id);
    }
}
