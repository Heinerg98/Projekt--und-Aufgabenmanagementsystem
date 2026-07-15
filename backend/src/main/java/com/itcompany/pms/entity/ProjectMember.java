package com.itcompany.pms.entity;

import jakarta.persistence.*;
<<<<<<< HEAD
import java.time.LocalDateTime;

@Entity
@Table(name = "project_members", uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "user_id"}))
public class ProjectMember {
=======

import java.time.LocalDateTime;

@Entity
@Table(name = "project_members", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"project_id", "user_id"})
})
public class ProjectMember {

>>>>>>> origin/main
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

<<<<<<< HEAD
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
=======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
>>>>>>> origin/main
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
<<<<<<< HEAD
    private LocalDateTime joinedAt;

    @PrePersist
    void prePersist() {
        if (joinedAt == null) {
            joinedAt = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
=======
    private LocalDateTime joinedAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
>>>>>>> origin/main
}
