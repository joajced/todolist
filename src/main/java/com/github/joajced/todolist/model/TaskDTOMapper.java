package com.github.joajced.todolist.model;

import org.springframework.stereotype.Service;

@Service
public class TaskDTOMapper {

    public TaskDTO toDTO(Task task) {

        return new TaskDTO(
                task.getId(),
                task.getContent(),
                task.isDone()
        );
    }

    public Task toEntity(TaskDTO dto) {

        return new Task(
                dto.getId(),
                dto.getContent(),
                dto.isDone()
        );
    }

}
