package com.Sprint2.sprint2.services;

import com.Sprint2.sprint2.dtos.TaskRecord;
import com.Sprint2.sprint2.exceptions.TaskException;
import com.Sprint2.sprint2.model.Task;

import java.util.Set;

public interface TaskService {
    Set<TaskRecord> getAllTasks();
    TaskRecord getOneTaskById(Long id);
    Task createTask(Long id_user, TaskRecord newTask);
    Task updateTask(Long task_id,TaskRecord newDataTask) throws TaskException;
    void deleteTask(Long id) throws TaskException;
    Set<TaskRecord> getTaskByUserId(Long id);
}
