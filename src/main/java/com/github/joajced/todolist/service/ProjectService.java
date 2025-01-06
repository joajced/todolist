package com.github.joajced.todolist.service;

import com.github.joajced.todolist.model.Project;
import com.github.joajced.todolist.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {

        this.projectRepository = projectRepository;
    }


    // GET requests

    public List<Project> getProjects() {

        return projectRepository.findAll();
    }

    public Project getProjectById(Long projectId) {

        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project with id " + projectId + " does not exist."));
    }


    // POST requests

    public Project createProject(Project project) {

        return projectRepository.save(project);
    }


    // PATCH requests

    public Project renameProject(Long projectId, Map<String, Object> projectFields) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project with id " + projectId + " does not exist."));

        projectFields.forEach((key, value) -> {

            if (key.equals("name")) project.setName((String) value);

        });

        return projectRepository.save(project);
    }


    // DELETE requests

    public void deleteProject(Long projectId) {

        projectRepository.deleteById(projectId);
    }
}
