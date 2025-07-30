package com.example.bms.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;
    
    @Column(name = "balance", precision = 15, scale = 2)
    private BigDecimal balance;
    
    @Column(name = "customer_id", nullable = false)
    private String customerId;
    
    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "is_active")
    private boolean isActive;
    
    public enum AccountType {
        SAVINGS,
        CURRENT,
        FIXED_DEPOSIT,
        RECURRING_DEPOSIT
    }
    
    // Constructors
    public Account() {
        this.balance = BigDecimal.ZERO;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Account(String accountNumber, String customerId, AccountType accountType) {
        this();
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Legacy method for backward compatibility
    public double getBalanceAsDouble() {
        return balance != null ? balance.doubleValue() : 0.0;
    }
    
    public void setBalance(double balance) {
        this.balance = BigDecimal.valueOf(balance);
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public AccountType getAccountType() {
        return accountType;
    }
    
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Utility methods
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            this.balance = this.balance.add(amount);
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public boolean withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0 && this.balance.compareTo(amount) >= 0) {
            this.balance = this.balance.subtract(amount);
            this.updatedAt = LocalDateTime.now();
            return true;
        }
        return false;
    }
}
