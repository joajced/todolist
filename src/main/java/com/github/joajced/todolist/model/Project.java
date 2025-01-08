package com.github.joajced.todolist.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private List<Task> tasks;

    @JsonProperty("total")
    public Long getTotalTasks() {

        if (tasks == null) return 0L;

        return ((Integer) tasks.size()).longValue();
    }

    @JsonProperty("completed")
    public Long getCompletedTasks() {

        long completed = 0L;

        if (tasks == null) return completed;

        for (Task task : tasks) {

            if (task.isDone()) completed++;
        }

        return completed;
    }
}
