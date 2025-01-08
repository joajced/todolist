package com.github.joajced.todolist.controller;

import com.github.joajced.todolist.model.Project;
import com.github.joajced.todolist.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins="http://127.0.0.1:5500")
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
    public Project getProjectById(@PathVariable Long id) {

        return projectService.getProjectById(id);
    }

    @PostMapping("/projects")
    public Project createProject(@RequestBody Project project) {

        return projectService.createProject(project);
    }

    @PatchMapping("/projects/{id}")
    public Project renameProject(@PathVariable Long id, @RequestBody Map<String, Object> fields) {

        return projectService.renameProject(id, fields);
    }

    @DeleteMapping("/projects/{id}")
    public void deleteProject(@PathVariable Long id) {

        projectService.deleteProject(id);
    }
}
