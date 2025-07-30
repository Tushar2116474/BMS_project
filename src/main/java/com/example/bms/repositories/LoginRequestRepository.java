package com.example.bms.repositories;

import com.example.bms.models.LoginRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoginRequestRepository extends JpaRepository<LoginRequest, Long> {
    
    // Find by login ID
    Optional<LoginRequest> findByLoginId(String loginId);
    
    // Find active tokens for a user
    @Query("SELECT lr FROM LoginRequest lr WHERE lr.loginId = :loginId AND lr.isTokenActive = true")
    List<LoginRequest> findActiveTokensByLoginId(@Param("loginId") String loginId);
    
    // Find by JWT token
    Optional<LoginRequest> findByJwtToken(String jwtToken);
    
    // Find by bearer token
    Optional<LoginRequest> findByBearerToken(String bearerToken);
    
    // Find active sessions (not expired)
    @Query("SELECT lr FROM LoginRequest lr WHERE lr.isTokenActive = true AND lr.tokenExpiresAt > :currentTime")
    List<LoginRequest> findActiveSessions(@Param("currentTime") LocalDateTime currentTime);
    
    // Find expired tokens
    @Query("SELECT lr FROM LoginRequest lr WHERE lr.isTokenActive = true AND lr.tokenExpiresAt <= :currentTime")
    List<LoginRequest> findExpiredTokens(@Param("currentTime") LocalDateTime currentTime);
    
    // Find by customer ID
    List<LoginRequest> findByCustomerId(String customerId);
    
    // Clean up expired tokens
    @Query("UPDATE LoginRequest lr SET lr.isTokenActive = false WHERE lr.tokenExpiresAt <= :currentTime")
    void deactivateExpiredTokens(@Param("currentTime") LocalDateTime currentTime);
}
