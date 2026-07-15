package com.itcompany.pms.repository;

import com.itcompany.pms.entity.Project;
import com.itcompany.pms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByProjectManager(User projectManager);
}
