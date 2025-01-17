package com.Sprint2.sprint2.services.impl;

import com.Sprint2.sprint2.dtos.NewUserRecord;
import com.Sprint2.sprint2.dtos.PatchUserRecord;
import com.Sprint2.sprint2.dtos.UserRecord;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.model.UserRoles;
import com.Sprint2.sprint2.repositories.UserRepository;
import com.Sprint2.sprint2.services.UserService;
import com.Sprint2.sprint2.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserRecord getUserRecord(Long id) throws UserException {
        UserEntity user=getUserById(id);
        return new UserRecord(user.getId(),user.getUsername(),user.getEmail());
    }

    public void createUser(NewUserRecord newuser) throws UserException {
        validateData(newuser.username(),newuser.password());
        UserEntity user = new UserEntity(newuser.username(),passwordEncoder.encode(newuser.password()),newuser.email(), UserRoles.USER);
        validateExistentUser(user.getEmail());
        userRepository.save(user);
    }

    public void createAdmin(NewUserRecord newuser) throws UserException{
        validateData(newuser.username(),newuser.password());
        UserEntity user = new UserEntity(newuser.username(),passwordEncoder.encode(newuser.password()),newuser.email(), UserRoles.ADMIN);
        validateExistentUser(user.getEmail());
        userRepository.save(user);
    }

    public void updateUser(String email, PatchUserRecord newuser) throws UserException {
        UserEntity user=userRepository.findByEmail(email).orElseThrow(()->new UserException(Constant.USR_NOT_EXIST, HttpStatus.NOT_FOUND));
        validateNewData(user,newuser);
        userRepository.save(user);
    }

    public void validateNewData(UserEntity user, PatchUserRecord newuser) throws UserException {
        if (newuser.username()!=null && !newuser.username().isBlank()){
            user.setUsername(newuser.username());
        }else{
            throw new UserException(Constant.EMPTY_US, HttpStatus.BAD_REQUEST);
        }
        if (newuser.password()!=null && !newuser.password().isBlank()){
            user.setPassword(passwordEncoder.encode(newuser.password()));
        }else{
            throw new UserException(Constant.EMPTY_PASS, HttpStatus.BAD_REQUEST);
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

    public void validateExistentUser(String email)throws UserException{
        if (userRepository.findByEmail(email).isPresent()){
            throw new UserException(Constant.EXIST_MAIL, HttpStatus.CONFLICT);
        }
    }

    public UserEntity getUserById(Long id) throws UserException {
        UserEntity user=userRepository.findById(id).orElseThrow(()->new UserException(Constant.USR_NOT_EXIST, HttpStatus.NOT_FOUND));
        return user;
    }


    public Long getIdByEmail(String email) throws UserException {
        UserEntity user=userRepository.findByEmail(email).orElseThrow(()->new UserException(Constant.USR_NOT_EXIST, HttpStatus.NOT_FOUND));
        return user.getId();
    }

    public void deleteUserByEmail(String email)  throws UserException {
        Long id=getIdByEmail(email);
        userRepository.deleteById(id);
    }

    public Set<UserRecord> getAllUsers(){
        List<UserEntity> users = userRepository.findAll();
        Set<UserRecord> userRecords= users.stream().map(userEntity -> new UserRecord(userEntity.getId(),userEntity.getUsername(),userEntity.getEmail())).collect(Collectors.toSet());
        return userRecords;
    }
}
