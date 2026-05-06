package com.varsha.taskmanager.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.varsha.taskmanager.model.Task;
import com.varsha.taskmanager.model.TaskStatus;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {
    
    //Paginated list for a specific assignee
    Page<Task> findByAssignedTo_Id(UUID assigneeId, Pageable pageable);

    long countByStatus(TaskStatus status);

    long countByAssignedTo_IdAndStatus(UUID assigneeId, TaskStatus status);

    //Overdue tasks
    @Query("SELECT COUNT(t) FROM Task t WHERE t.dueDate < CURRENT_DATE AND t.status <> 'DONE'")
    long countOverdueTasks();
}
