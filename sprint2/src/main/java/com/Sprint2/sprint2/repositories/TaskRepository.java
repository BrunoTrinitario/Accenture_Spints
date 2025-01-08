package com.Sprint2.sprint2.repositories;

import com.Sprint2.sprint2.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findById(Long id);

    boolean existsById(Long id);

    List<Task> findByUserId(Long id);

    void deleteById(Long id);

}
