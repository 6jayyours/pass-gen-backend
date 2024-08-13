package com.app.password_generator.repository;

import com.app.password_generator.entity.Passwords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PasswordsRepository extends JpaRepository<Passwords, Long> {

    @Query("SELECT p FROM Passwords p WHERE p.user.id = :userId")
    Set<Passwords> findByUserId(@Param("userId") Long userId);
}
