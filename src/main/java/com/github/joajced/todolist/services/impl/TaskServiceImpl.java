package com.github.joajced.todolist.services.impl;

import com.github.joajced.todolist.model.Task;
import com.github.joajced.todolist.repository.TaskRepository;
import com.github.joajced.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

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

        if (optionalTask.isPresent())
            return optionalTask.get();
        throw new RuntimeException("Task with id " + id + " does not exist.");

    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, Task newTask) {

        Optional<Task> needsUpdate = taskRepository.findById(id);

        if (needsUpdate.isPresent()) {
            needsUpdate.get().setContent(newTask.getContent());
            needsUpdate.get().setDone(newTask.isDone());

            return taskRepository.save(needsUpdate.get());
        }
        throw new RuntimeException("Task with id " + id + " does not exist.");

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
