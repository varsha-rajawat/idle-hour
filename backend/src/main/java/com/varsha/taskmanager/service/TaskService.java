package com.varsha.taskmanager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varsha.taskmanager.dto.CreateTaskRequest;
import com.varsha.taskmanager.dto.TaskResponse;
import com.varsha.taskmanager.exception.ResourceNotFoundException;
import com.varsha.taskmanager.model.Task;
import com.varsha.taskmanager.model.User;
import com.varsha.taskmanager.repository.TaskRepository;
import com.varsha.taskmanager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    //Create a new task
    @Transactional
    public TaskResponse create(CreateTaskRequest request, User creator){
        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getAssigneeId()));
        }

        Task task = Task.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .priority(request.getPriority())
            .createdBy(creator)
            .assignedTo(assignee)
            .dueDate(request.getDueDate())
            .build();
        
        return TaskResponse.fromEntity(taskRepository.save(task));
    }
    
}
