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

    private UserRol userRol;

    public UserEntity() {
    }

    public UserEntity(String username, String password, String email, UserRol userRol) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userRol = userRol;
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

    public UserRol getUserRol() {
        return userRol;
    }

    public void setUserRol(UserRol userRol) {
        this.userRol = userRol;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
