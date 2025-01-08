package com.Sprint2.sprint2.model;

import jakarta.persistence.*;
import org.hibernate.action.internal.OrphanRemovalAction;

import java.util.HashSet;
import java.util.Set;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = true)
    private String email;

    @Column(unique = false, nullable = true)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks= new HashSet<>();;

    public UserEntity(){
    }

    public UserEntity(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addTask(Task task){
        this.tasks.add(task);
        task.setUser(this);
    }

    public void removeTask(Task task){
        this.tasks.remove(task);
        task.setUser(null);
    }

    public Long getId() {
        return id;
    }
}
