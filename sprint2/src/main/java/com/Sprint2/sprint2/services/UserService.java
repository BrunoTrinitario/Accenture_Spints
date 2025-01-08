package com.Sprint2.sprint2.services;

import com.Sprint2.sprint2.dtos.UserRecord;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.UserEntity;

public interface UserService {

    UserRecord getUserRecord(Long id) throws UserException;

    void createUser(UserRecord newuser) throws UserException;

    void deleteUserbyID(Long id) throws UserException;

    void updateUser(UserRecord newuser) throws UserException;

    UserEntity getUserById(Long id) throws UserException;

}
