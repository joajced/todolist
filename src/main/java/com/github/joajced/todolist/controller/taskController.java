package com.github.joajced.todolist.controller;

import com.github.joajced.todolist.repository.Repository;
import com.github.joajced.todolist.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        throw new RuntimeException("Task with id " + id + " couldn't be found");
    }

    @PostMapping("/api/tasks")
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @DeleteMapping("/api/tasks/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }

}
