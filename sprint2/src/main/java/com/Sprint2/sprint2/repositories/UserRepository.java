package com.Sprint2.sprint2.repositories;

import com.Sprint2.sprint2.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findById(Long id);

    boolean existsById(Long id);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    void deleteById(Long id);

    void deleteByEmail(String email);


}
