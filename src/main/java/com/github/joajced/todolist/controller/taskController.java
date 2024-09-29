package com.github.joajced.todolist.controller;

import com.github.joajced.todolist.repository.Repository;
import com.github.joajced.todolist.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @PostMapping("/api/tasks")
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

}
