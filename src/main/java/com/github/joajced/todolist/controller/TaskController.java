package com.github.joajced.todolist.controller;

import com.github.joajced.todolist.model.TaskDTO;
import com.github.joajced.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins="http://127.0.0.1:5500")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {

        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public List<TaskDTO> getTasks() {

        return taskService.getTasks();
    }

    @GetMapping("/tasks/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {

        return taskService.getTaskById(id);
    }

    @PostMapping("/tasks")
    public TaskDTO createTask(@RequestBody TaskDTO task) {

        return taskService.createTask(task);
    }

    @PatchMapping("/tasks/{id}")
    public TaskDTO patchTask(@PathVariable Long id, @RequestBody Map<String, Object> fields) {

        return taskService.patchTask(id, fields);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Long id) {

        taskService.deleteTask(id);
    }
}
