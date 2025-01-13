package com.Sprint2.sprint2.services;

import com.Sprint2.sprint2.dtos.NewUserRecord;
import com.Sprint2.sprint2.dtos.PatchUserRecord;
import com.Sprint2.sprint2.dtos.UserRecord;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.UserEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface UserService {

    UserRecord getUserRecord(Long id) throws UserException;

    void createUser(NewUserRecord newuser) throws UserException;

    void createAdmin(NewUserRecord newuser) throws UserException;

    void updateUser(String email, PatchUserRecord newuser) throws UserException;

    UserEntity getUserById(Long id) throws UserException;

    Long getIdByEmail(String email) throws UserException;

    void deleteUserByEmail(String email)  throws UserException ;

    Set<UserRecord> getAllUsers();
}
