package com.github.joajced.todolist.controller;

import com.github.joajced.todolist.model.Task;
import com.github.joajced.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {

        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public List<Task> getTasks() {

        return taskService.getTasks();
    }

    @GetMapping("/tasks/{id}")
    public Task getTaskById(@PathVariable Long id) {

        return taskService.getTaskById(id);
    }

    @PostMapping("/tasks")
    public Task createTask(@RequestBody Task task) {

        return taskService.createTask(task);
    }

    @PatchMapping("/tasks/{id}")
    public Task patchTask(@PathVariable Long id, @RequestBody Map<String, Object> fields) {

        return taskService.patchTask(id, fields);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Long id) {

        taskService.deleteTask(id);
    }
}
