package com.github.joajced.todolist.service;

import com.github.joajced.todolist.model.Project;
import com.github.joajced.todolist.model.Task;
import com.github.joajced.todolist.repository.ProjectRepository;
import com.github.joajced.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {

        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }


    // GET requests

    public List<Task> getTasks() {

        return taskRepository.findAll();
    }

    public Task getTaskById(Long taskId) {

        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task with id " + taskId + " does not exist."));
    }

    public List<Task> getTasksByProjectId(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project with id " + projectId + "does not exist."));

        return project.getTasks();
    }


    // POST requests

    public Task createTask(Task task) {

        return taskRepository.save(task);
    }


    // PATCH requests

    public Task patchTask(Long taskId, Map<String, Object> taskFields) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task with id " + taskId + " does not exist."));

        taskFields.forEach((key, value) -> {

            switch (key) {

                case "content":

                    task.setContent((String) value);
                    break;

                case "done":

                    task.setDone((Boolean) value);
                    break;

                case "project":

                    Long projectId = (Long) value;
                    Project project = projectRepository.findById(projectId)
                            .orElseThrow(() -> new RuntimeException("Project with id " + projectId + " does not exist."));

                    task.setProject(project);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown field: " + key);
            }
        });

        return taskRepository.save(task);
    }

    // DELETE requests

    public void deleteTask(Long taskId) {

        taskRepository.deleteById(taskId);
    }
}
