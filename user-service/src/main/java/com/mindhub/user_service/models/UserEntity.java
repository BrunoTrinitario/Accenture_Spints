package com.mindhub.user_service.models;

import jakarta.persistence.*;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false, nullable = false)
    private String username;

    @Column(unique = true, nullable = true)
    private String email;

    @Column(unique = false, nullable = true)
    private String password;

    private UserRole userRole;

    public UserEntity() {
    }

    public UserEntity(String username, String password, String email, UserRole userRol) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userRole = userRol;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
