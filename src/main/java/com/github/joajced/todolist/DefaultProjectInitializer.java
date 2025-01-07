package com.github.joajced.todolist;

import com.github.joajced.todolist.model.Project;
import com.github.joajced.todolist.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultProjectInitializer implements CommandLineRunner {

    @Autowired
    private final ProjectRepository projectRepository;

    public DefaultProjectInitializer(ProjectRepository projectRepository) {

        this.projectRepository = projectRepository;
    }

    @Override
    public void run(String... args) {

        Project defaultProject = new Project();
        defaultProject.setName("My Tasks");
        projectRepository.save(defaultProject);
    }
}
