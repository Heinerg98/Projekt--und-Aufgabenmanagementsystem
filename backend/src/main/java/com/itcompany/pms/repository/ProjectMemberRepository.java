package com.itcompany.pms.repository;

import com.itcompany.pms.entity.Project;
import com.itcompany.pms.entity.ProjectMember;
import com.itcompany.pms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    Optional<ProjectMember> findByProjectAndUser(Project project, User user);
    List<ProjectMember> findByProject(Project project);
    List<ProjectMember> findByUser(User user);
}
