package com.Sprint2.sprint2.services;

import com.Sprint2.sprint2.dtos.TaskRecord;
import com.Sprint2.sprint2.exceptions.TaskException;

import java.util.Set;

public interface TaskService {
    TaskRecord getOneTaskById(Long id);
    void createTask(Long id_user, TaskRecord newTask);
    void updateTask(TaskRecord newDataTask) throws TaskException;
    void deleteTask(Long id) throws TaskException;
    Set<TaskRecord> getTaskByUserId(Long id);
}
