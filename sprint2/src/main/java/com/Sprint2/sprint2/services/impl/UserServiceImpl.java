package com.Sprint2.sprint2.services.impl;

import com.Sprint2.sprint2.dtos.UserRecord;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.repositories.UserRepository;
import com.Sprint2.sprint2.services.UserService;
import com.Sprint2.sprint2.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public UserRecord getUserRecord(Long id) throws UserException {
        UserEntity user=getUserById(id);
        return new UserRecord(user.getId(),user.getUsername(),user.getEmail());
    }

    public void createUser(UserRecord newuser) throws UserException {
        UserEntity user = new UserEntity(newuser.username(),generateRandomPassword(6),newuser.email());
        validateData(user.getUsername(),user.getPassword());
        validateExistentUser(user.getUsername(),user.getEmail());
        userRepository.save(user);
    }

    public void deleteUserbyID(Long id) throws UserException {
        if (!userRepository.existsById(id)){
            throw new UserException(Constant.USR_NOT_EXIST, HttpStatus.NOT_FOUND);
        }else {
            userRepository.deleteById(id);
        }
    }

    public void updateUser(UserRecord newuser) throws UserException {
        UserEntity user=userRepository.findById(newuser.id()).orElseThrow(()->new UserException(Constant.USR_NOT_EXIST, HttpStatus.NOT_FOUND));
        validateNewData(user,newuser);
        userRepository.save(user);
    }

    public void validateNewData(UserEntity user, UserRecord newuser) {
        if (newuser.username()!=null && !newuser.username().isBlank()){
            user.setUsername(newuser.username());
        }
        if (newuser.email()!=null && !newuser.email().isBlank()){
            user.setEmail(newuser.email());
        }
    }

    public void validateData(String username, String password) throws UserException {
        if (password==null || password.isBlank()) {
            throw new UserException(Constant.EMPTY_PASS, HttpStatus.BAD_REQUEST);
        }
        if (username==null || username.isBlank()) {
            throw new UserException(Constant.EMPTY_US, HttpStatus.BAD_REQUEST);
        }

    }

    public void validateExistentUser(String username, String email)throws UserException{
        if (userRepository.findByUsername(username).isPresent()){
            throw new UserException(Constant.EXIST_US, HttpStatus.CONFLICT);
        }
        if (userRepository.findByEmail(email).isPresent()){
            throw new UserException(Constant.EXIST_MAIL, HttpStatus.CONFLICT);
        }
    }

    public UserEntity getUserById(Long id) throws UserException {
        UserEntity user=userRepository.findById(id).orElseThrow(()->new UserException(Constant.USR_NOT_EXIST, HttpStatus.NOT_FOUND));
        return user;

    }

    private String generateRandomPassword(int pass_length){
        String CHARACTERS = Constant.ABC;
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(pass_length);

        for (int i = 0; i < pass_length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }

}
