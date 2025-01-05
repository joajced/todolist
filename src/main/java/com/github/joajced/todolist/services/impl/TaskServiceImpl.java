package com.github.joajced.todolist.services.impl;

import com.github.joajced.todolist.model.Task;
import com.github.joajced.todolist.model.TaskDTO;
import com.github.joajced.todolist.model.TaskDTOMapper;
import com.github.joajced.todolist.repository.TaskRepository;
import com.github.joajced.todolist.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskDTOMapper taskDTOMapper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, TaskDTOMapper taskDTOMapper) {

        this.taskRepository = taskRepository;
        this.taskDTOMapper = taskDTOMapper;
    }

    @Override
    public List<TaskDTO> getTasks() {

        return taskRepository.findAll()
                .stream()
                .map(taskDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(Long id) {

        Optional<TaskDTO> optionalTask = taskRepository.findById(id)
                                            .map(taskDTOMapper::toDTO);

        if (optionalTask.isPresent()) return optionalTask.get();

        throw new RuntimeException("Task with id " + id + " does not exist.");
    }

    @Override
    public TaskDTO createTask(TaskDTO task) {

        Task createdTask = taskDTOMapper.toEntity(task);

        taskRepository.save(createdTask);

        return taskDTOMapper.toDTO(createdTask);
    }

    @Override
    public TaskDTO patchTask(Long id, Map<String, Object> fields) {

        Optional<TaskDTO> needsPatch = taskRepository.findById(id)
                                           .map(taskDTOMapper::toDTO);

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

            Task patchedTask = taskDTOMapper.toEntity(needsPatch.get());

            taskRepository.save(patchedTask);

            return taskDTOMapper.toDTO(patchedTask);

        }

        throw new RuntimeException("Task with id " + id + " does not exist.");
    }

    @Override
    public void deleteTask(Long id) {

        taskRepository.deleteById(id);
    }
}
