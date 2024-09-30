package com.github.joajced.todolist.controller;

import com.github.joajced.todolist.repository.Repository;
import com.github.joajced.todolist.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class taskController {

    @Autowired
    private Repository taskRepository;

    @GetMapping("/api")
    public String welcome() {
        return "Welcome to your to-do list!";
    }

    @GetMapping("/api/tasks")
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @GetMapping("/api/tasks/{id}")
    public Task getTaskById(@PathVariable Long id) {

        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent())
            return optionalTask.get();
        throw new RuntimeException("Task with id " + id + " does not exist.");
    }

    @PostMapping("/api/tasks")
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @PutMapping("/api/tasks/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task newTask) {

        Optional<Task> needsUpdate = taskRepository.findById(id);

        if (needsUpdate.isPresent()) {
            needsUpdate.get().setContent(newTask.getContent());
            needsUpdate.get().setDone(newTask.isDone());

            return taskRepository.save(needsUpdate.get());
        }
        throw new RuntimeException("Task with id " + id + " does not exist.");
    }

    @PatchMapping("/api/tasks/{id}")
    public Task patchTask(@PathVariable Long id, @RequestBody Map<String, Object> fields) {

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

    @DeleteMapping("/api/tasks/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }

}
