package com.Sprint2.sprint2.services;

import com.Sprint2.sprint2.dtos.NewTaskRecord;
import com.Sprint2.sprint2.dtos.TaskRecord;
import com.Sprint2.sprint2.exceptions.TaskException;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.Task;

import java.util.Set;

public interface TaskService {
    Set<TaskRecord> getAllTasks();
    TaskRecord getOneTaskById(String email,Long id) throws TaskException;
    TaskRecord createTask(String email, NewTaskRecord newTask);
    Task updateTask(String email, Long task_id,NewTaskRecord newDataTask) throws TaskException;
    void deleteTask(String email,Long id) throws TaskException;
    Set<TaskRecord> getTaskByUserEmail(String email) throws TaskException;
}
