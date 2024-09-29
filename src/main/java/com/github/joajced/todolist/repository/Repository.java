package com.github.joajced.todolist.repository;

import com.github.joajced.todolist.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<Task, Long>{
}
