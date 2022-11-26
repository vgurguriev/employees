package com.example.pairofemployees.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long firstEmployee;

    private long secondEmployee;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Project> projects = new ArrayList<>();

    public Team(long id, long firstEmployee, long secondEmployee, List<Project> projects) {
        this.id = id;
        this.firstEmployee = firstEmployee;
        this.secondEmployee = secondEmployee;
        this.projects = projects;
    }

    public Team() {}

    public long getId() {
        return id;
    }

    public Team setId(long id) {
        this.id = id;
        return this;
    }

    public long getFirstEmployee() {
        return firstEmployee;
    }

    public Team setFirstEmployee(long firstEmployee) {
        this.firstEmployee = firstEmployee;
        return this;
    }

    public long getSecondEmployee() {
        return secondEmployee;
    }

    public Team setSecondEmployee(long secondEmployee) {
        this.secondEmployee = secondEmployee;
        return this;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public Team setProjects(List<Project> projects) {
        this.projects = projects;
        return this;
    }
}
