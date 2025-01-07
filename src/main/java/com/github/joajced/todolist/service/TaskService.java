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

        // Assign default project if no project was assigned at creation.
        if (task.getProject() == null) {

            Project defaultProject = projectRepository.findById(((Integer) 1).longValue())
                    .orElseThrow(() -> new RuntimeException("Default project (id: 1) doesn't exist."));

            task.setProject(defaultProject);
        }

        return taskRepository.save(task);
    }


    // PATCH requests

    public Task patchTask(Long taskId, Map<String, Object> fields) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task with id " + taskId + " does not exist."));

        fields.forEach((key, value) -> {

            switch (key) {

                case "content":

                    task.setContent((String) value);
                    break;

                case "done":

                    task.setDone((Boolean) value);
                    break;

                case "project_id":

                    /*  The number given as JSON input is automatically
                     *  interpreted as Integer, which is why it needs to
                     *  be converted into a Long
                     */
                    Long projectId = ((Integer) value).longValue();
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
