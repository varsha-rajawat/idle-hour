package com.varsha.taskmanager.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.varsha.taskmanager.model.RefreshToken;
import com.varsha.taskmanager.model.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);

    //Used on logout - delete all tokens for this user (all devices)
    void deleteByUser(User user);

    //Used to count active tokens for a user (optional, for monitoring)
    long countByUser(User user);
    
}
