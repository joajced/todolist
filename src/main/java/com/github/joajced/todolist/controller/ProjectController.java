package com.github.joajced.todolist.controller;

import com.github.joajced.todolist.model.Project;
import com.github.joajced.todolist.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {

        this.projectService = projectService;
    }

    @GetMapping("/projects")
    public List<Project> getProjects() {

        return projectService.getProjects();
    }

    @GetMapping("/projects/{id}")
    public Project getProjectById(@PathVariable Long projectId) {

        return projectService.getProjectById(projectId);
    }

    @PostMapping("/projects")
    public Project createProject(@RequestBody Project project) {

        return projectService.createProject(project);
    }

    @PatchMapping("/projects/{id}")
    public Project renameProject(@PathVariable Long projectId, @RequestBody Map<String, Object> projectFields) {

        return projectService.renameProject(projectId, projectFields);
    }

    @DeleteMapping("/projects/{id}")
    public void deleteProject(@PathVariable Long projectId) {

        projectService.deleteProject(projectId);
    }
}
