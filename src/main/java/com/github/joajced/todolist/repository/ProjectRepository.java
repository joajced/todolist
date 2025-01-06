package com.github.joajced.todolist.repository;

import com.github.joajced.todolist.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
