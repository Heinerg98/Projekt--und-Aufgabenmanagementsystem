package com.itcompany.pms.repository;

import com.itcompany.pms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
<<<<<<< HEAD
=======
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
>>>>>>> origin/main
}
