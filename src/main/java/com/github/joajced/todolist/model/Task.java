package com.github.joajced.todolist.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @Column
    private boolean isDone;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonBackReference
    private Project project;

    @JsonProperty("project_id")
    public Long getProjectId() {

        return this.project != null ? this.project.getId() : null;
    }
}
