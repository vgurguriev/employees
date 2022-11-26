package com.example.pairofemployees.entity;

import javax.persistence.*;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long projectId;

    private long totalTime;

    @ManyToOne
    private Team team;


    public Project(long id, long projectId, long totalTime) {
        this.id = id;
        this.projectId = projectId;
        this.totalTime = totalTime;
    }

    public Project() {}

    public long getId() {
        return id;
    }

    public Project setId(long id) {
        this.id = id;
        return this;
    }

    public long getProjectId() {
        return projectId;
    }

    public Project setProjectId(long projectId) {
        this.projectId = projectId;
        return this;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public Project setTotalTime(long totalTime) {
        this.totalTime = totalTime;
        return this;
    }

    public Team getTeam() {
        return team;
    }

    public Project setTeam(Team team) {
        this.team = team;
        return this;
    }
}
