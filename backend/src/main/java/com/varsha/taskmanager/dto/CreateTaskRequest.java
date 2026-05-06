package com.varsha.taskmanager.dto;

import com.varsha.taskmanager.model.TaskPriority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateTaskRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    private String description;

    private TaskPriority priority = TaskPriority.MEDIUM;

    private UUID assigneeId;

    @FutureOrPresent(message = "Due date cannot be in the past")
    private LocalDate dueDate;
}
