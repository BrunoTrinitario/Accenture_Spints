package com.mindhub.user_service.services;

import com.mindhub.user_service.dtos.NewUserRecord;
import com.mindhub.user_service.dtos.UpdateUserRecord;
import com.mindhub.user_service.dtos.UserRecord;
import com.mindhub.user_service.exceptions.UserException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface UserService {
    Set<UserRecord> getAllUsers();

    UserRecord getUserById(Long id) throws UserException;

    UserRecord getUserByEmail(String email) throws UserException;

    UserRecord createUser(NewUserRecord newUserRecord) throws UserException;

    UserRecord createAdmin(NewUserRecord newUserRecord) throws UserException;

    UserRecord updateUser(Long id, UpdateUserRecord updateUserRecord) throws UserException;

    void deleteUserById(Long id) throws UserException;

    void deleteUserByEmail(String email) throws UserException;

    boolean existUserByid(Long id) throws UserException;

    boolean existUserByEmail(String email) throws UserException;

    Long getIdByEmail(String email) throws UserException;

}
