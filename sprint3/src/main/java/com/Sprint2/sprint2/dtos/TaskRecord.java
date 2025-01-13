package com.Sprint2.sprint2.dtos;

import com.Sprint2.sprint2.model.TaskStatus;

public record TaskRecord(Long id, String title, String description, TaskStatus status) {

}
