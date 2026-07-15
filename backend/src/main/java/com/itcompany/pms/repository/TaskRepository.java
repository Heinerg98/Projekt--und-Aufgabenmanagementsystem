package com.itcompany.pms.repository;

import com.itcompany.pms.entity.Project;
import com.itcompany.pms.entity.Task;
import com.itcompany.pms.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);
    long countByProject(Project project);
    long countByProjectAndStatus(Project project, TaskStatus status);
}
