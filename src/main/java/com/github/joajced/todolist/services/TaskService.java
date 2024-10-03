package com.github.joajced.todolist.services;

import com.github.joajced.todolist.model.TaskDTO;

import java.util.List;
import java.util.Map;

public interface TaskService {

    List<TaskDTO> getTasks();

    TaskDTO getTaskById(Long id);

    TaskDTO createTask(TaskDTO task);

    TaskDTO patchTask(Long id, Map<String, Object> fields);

    void deleteTask(Long id);

}
