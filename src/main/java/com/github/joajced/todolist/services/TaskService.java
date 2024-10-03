package com.github.joajced.todolist.services;

import com.github.joajced.todolist.model.Task;

import java.util.List;
import java.util.Map;

public interface TaskService {

    List<Task> getTasks();

    Task getTaskById(Long id);

    Task createTask(Task task);

    Task updateTask(Long id, Task newTask);

    Task patchTask(Long id, Map<String, Object> fields);

    void deleteTask(Long id);

}
