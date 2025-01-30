package com.mindhub.user_service.services.imp;

import com.mindhub.user_service.config.JwtUtils;
import com.mindhub.user_service.config.RabbitMQConfig;
import com.mindhub.user_service.dtos.*;
import com.mindhub.user_service.exceptions.UserException;
import com.mindhub.user_service.models.UserEntity;
import com.mindhub.user_service.models.UserRole;
import com.mindhub.user_service.repositories.UserRepository;
import com.mindhub.user_service.services.UserService;
import com.mindhub.user_service.util.Constants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserServiceImp implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Set<UserRecord> getAllUsers() {
        List<UserEntity> listUsers = userRepository.findAll();
        Set<UserRecord> setUsers = listUsers.stream().map(user -> new UserRecord(user.getId(),user.getUsername(), user.getEmail(), user.getUserRole())).collect(Collectors.toSet());
        return setUsers;
    }

    @Override
    public String loginUser(LoginUserRecord loginUserRecord) throws UserException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUserRecord.email(),
                            loginUserRecord.password()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserRecord user = getUserByEmail(loginUserRecord.email());

            String jwt = jwtUtils.generateToken(user.email(),user.id(),user.rol().toString());

            return jwt;

        } catch (BadCredentialsException e) {
            throw new UserException(Constants.INV_CRED, HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public UserRecord getUserById(Long id) throws UserException {
        UserEntity user = userRepository.findById(id).orElseThrow(()->new UserException(Constants.USR_NOT_EXIST, HttpStatus.NOT_FOUND));
        UserRecord userRecord = new UserRecord(user.getId(), user.getUsername(),user.getEmail(),user.getUserRole());
        return userRecord;
    }

    @Override
    public UserRecord getUserByEmail(String email) throws UserException {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(()->new UserException(Constants.USR_NOT_EXIST, HttpStatus.NOT_FOUND));
        UserRecord userRecord = new UserRecord(user.getId(), user.getUsername(),user.getEmail(),user.getUserRole());
        return userRecord;
    }

    @Override
    public UserRecord createUser(NewUserRecord newUserRecord) throws UserException {
        validateUsername(newUserRecord.username());
        validatePassword(newUserRecord.password());
        validateEmail(newUserRecord.email());
        UserEntity userEntity = new UserEntity(newUserRecord.username(), passwordEncoder.encode(newUserRecord.password()), newUserRecord.email(), UserRole.USER);
        userEntity = userRepository.save(userEntity);
        UserRecord userRecord = new UserRecord(userEntity.getId(), userEntity.getUsername(),userEntity.getEmail(),userEntity.getUserRole());
        sendRegistrationEmail(userEntity);
        return userRecord;
    }

    @Override
    public UserRecord createAdmin(NewUserRecord newUserRecord) throws UserException {
        validateUsername(newUserRecord.username());
        validatePassword(newUserRecord.password());
        validateEmail(newUserRecord.email());
        UserEntity userEntity = new UserEntity(newUserRecord.username(), passwordEncoder.encode(newUserRecord.password()), newUserRecord.email(), UserRole.ADMIN);
        userEntity = userRepository.save(userEntity);
        UserRecord userRecord = new UserRecord(userEntity.getId(), userEntity.getUsername(),userEntity.getEmail(),userEntity.getUserRole());
        sendRegistrationEmail(userEntity);
        return userRecord;
    }

    private void sendRegistrationEmail(UserEntity userEntity){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "user.email",
                new EmailEvent(userEntity.getEmail(), Constants.SUC_REG,Constants.BODY_MAIL+userEntity.getUsername()));
    }

    @Override
    public UserRecord updateUser(Long id, UpdateUserRecord updateUserRecord) throws UserException {
        validateUsername(updateUserRecord.username());
        validatePassword(updateUserRecord.password());
        UserEntity user = userRepository.findById(id).orElseThrow(()->new UserException(Constants.USR_NOT_EXIST, HttpStatus.NOT_FOUND));
        user.setUsername(updateUserRecord.username());
        user.setPassword(updateUserRecord.password());
        user = userRepository.save(user);
        UserRecord userRecord = new UserRecord(user.getId(), user.getUsername(),user.getEmail(),user.getUserRole());
        return userRecord;
    }

    @Override
    public void deleteUserById(Long id) throws UserException {
        if (!existUserByid(id)){
            throw new UserException(Constants.USR_NOT_EXIST, HttpStatus.NOT_FOUND);
        }else{
            userRepository.deleteById(id);
        }

    }

    @Override
    public void deleteUserByEmail(String email) throws UserException {
        if(!existUserByEmail(email)){
            throw new UserException(Constants.USR_NOT_EXIST, HttpStatus.NOT_FOUND);
        }else
            userRepository.deleteByEmail(email);
    }

    @Override
    public boolean existUserByid(Long id) throws UserException {
        return userRepository.existsById(id);
    }

    @Override
    public boolean existUserByEmail(String email) throws UserException {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Long getIdByEmail(String email) throws UserException {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(()->new UserException(Constants.USR_NOT_EXIST, HttpStatus.NOT_FOUND));
        return user.getId();
    }

    private void validateUsername(String username) throws UserException {
        if (username==null || username.isBlank()) {
            throw new UserException(Constants.EMPTY_US);
        }
    }

    private void validatePassword(String password) throws UserException {
        if (password==null || password.isBlank()) {
            throw new UserException(Constants.EMPTY_PASS);
        }
    }

    private void validateEmail(String email) throws UserException {
        if (existUserByEmail(email)){
            throw new UserException(Constants.EXIST_EMAIL,HttpStatus.CONFLICT);
        }else{
            if (!validMail(email)){
                throw new UserException(Constants.INV_EMAIL,HttpStatus.CONFLICT);
            }
        }
    }

    public boolean validMail(String email){
        for (String dom : Constants.URL_MAILS) {
            if (email.endsWith(dom)) {
                return true;
            }
        }
        return false;
    }

}
