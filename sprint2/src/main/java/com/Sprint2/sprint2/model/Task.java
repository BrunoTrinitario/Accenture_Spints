package com.Sprint2.sprint2.model;

import jakarta.persistence.*;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String title,description;

    @Column(nullable = false)
    private TaskStatus status;

    @ManyToOne
    private UserEntity user;

    public Task(){

    }

    public Task(String title, String description, TaskStatus status, UserEntity user) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.user = user;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
        user.addTask(this);
    }

    public Long getId() {
        return id;
    }
}
