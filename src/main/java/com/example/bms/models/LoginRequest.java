package com.example.bms.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_requests")
public class LoginRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "jwt_token")
    private String jwtToken;
    
    @Column(name = "bearer_token")
    private String bearerToken;
    
    @Column(name = "customer_id")
    private String customerId;
    
    @Column(name = "customer_name")
    private String customerName;
    
    @Column(name = "customer_email")
    private String customerEmail;
    
    @Column(name = "customer_role")
    private String customerRole;
    
    @Column(name = "token_issued_at")
    private LocalDateTime tokenIssuedAt;
    
    @Column(name = "token_expires_at")
    private LocalDateTime tokenExpiresAt;
    
    @Column(name = "is_token_active")
    private boolean isTokenActive;
    
    @Column(name = "login_timestamp")
    private LocalDateTime loginTimestamp;
    
    @Column(name = "last_activity")
    private LocalDateTime lastActivity;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getJwtToken() {
        return jwtToken;
    }
    
    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
    
    public String getBearerToken() {
        return bearerToken;
    }
    
    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    
    public String getCustomerRole() {
        return customerRole;
    }
    
    public void setCustomerRole(String customerRole) {
        this.customerRole = customerRole;
    }
    
    public LocalDateTime getTokenIssuedAt() {
        return tokenIssuedAt;
    }
    
    public void setTokenIssuedAt(LocalDateTime tokenIssuedAt) {
        this.tokenIssuedAt = tokenIssuedAt;
    }
    
    public LocalDateTime getTokenExpiresAt() {
        return tokenExpiresAt;
    }
    
    public void setTokenExpiresAt(LocalDateTime tokenExpiresAt) {
        this.tokenExpiresAt = tokenExpiresAt;
    }
    
    public boolean isTokenActive() {
        return isTokenActive;
    }
    
    public void setTokenActive(boolean tokenActive) {
        isTokenActive = tokenActive;
    }
    
    public LocalDateTime getLoginTimestamp() {
        return loginTimestamp;
    }
    
    public void setLoginTimestamp(LocalDateTime loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }
    
    public LocalDateTime getLastActivity() {
        return lastActivity;
    }
    
    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }
    
    // Utility methods for JWT token management
    public boolean isTokenExpired() {
        return tokenExpiresAt != null && LocalDateTime.now().isAfter(tokenExpiresAt);
    }
    
    public boolean isValidToken() {
        return jwtToken != null && !jwtToken.isEmpty() && isTokenActive && !isTokenExpired();
    }
    
    public boolean isValidBearerToken() {
        return bearerToken != null && !bearerToken.isEmpty() && isTokenActive && !isTokenExpired();
    }
    
    public void invalidateToken() {
        this.isTokenActive = false;
        this.jwtToken = null;
        this.bearerToken = null;
    }
    
    // Method to generate bearer token with customer details
    public String generateBearerTokenHeader() {
        if (bearerToken != null && !bearerToken.isEmpty()) {
            return "Bearer " + bearerToken;
        }
        return null;
    }
    
    // Method to set customer details for the session
    public void setCustomerDetails(String customerId, String customerName, String customerEmail, String customerRole) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerRole = customerRole;
    }
    
    // Method to get customer info as a formatted string
    public String getCustomerInfo() {
        return String.format("Customer ID: %s, Name: %s, Email: %s, Role: %s", 
                           customerId, customerName, customerEmail, customerRole);
    }
}
