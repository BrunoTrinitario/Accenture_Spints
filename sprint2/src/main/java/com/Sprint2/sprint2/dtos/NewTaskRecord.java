package com.Sprint2.sprint2.dtos;

import com.Sprint2.sprint2.model.TaskStatus;

public record NewTaskRecord(String title, String description, TaskStatus status) {
}
