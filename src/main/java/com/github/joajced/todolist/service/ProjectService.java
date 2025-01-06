package com.github.joajced.todolist.service;

import com.github.joajced.todolist.model.Project;
import com.github.joajced.todolist.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Project renameProject(Long projectId, String newName) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project with id " + projectId + " does not exist."));

        project.setName(newName);

        return projectRepository.save(project);
    }


    // DELETE requests

    public void deleteProject(Long projectId) {

        projectRepository.deleteById(projectId);
    }
}
