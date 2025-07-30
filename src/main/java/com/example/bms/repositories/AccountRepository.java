package com.example.bms.repositories;

import com.example.bms.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    // Find by account number
    Optional<Account> findByAccountNumber(String accountNumber);
    
    // Find accounts by customer ID
    List<Account> findByCustomerId(String customerId);
    
    // Find active accounts by customer ID
    List<Account> findByCustomerIdAndIsActive(String customerId, boolean isActive);
    
    // Find by account type
    List<Account> findByAccountType(Account.AccountType accountType);
    
    // Find accounts by customer and account type
    List<Account> findByCustomerIdAndAccountType(String customerId, Account.AccountType accountType);
    
    // Find accounts with balance greater than specified amount
    List<Account> findByBalanceGreaterThan(BigDecimal amount);
    
    // Find accounts with balance between range
    @Query("SELECT a FROM Account a WHERE a.balance BETWEEN :minBalance AND :maxBalance")
    List<Account> findByBalanceRange(@Param("minBalance") BigDecimal minBalance, @Param("maxBalance") BigDecimal maxBalance);
    
    // Get total balance for a customer
    @Query("SELECT SUM(a.balance) FROM Account a WHERE a.customerId = :customerId AND a.isActive = true")
    BigDecimal getTotalBalanceByCustomerId(@Param("customerId") String customerId);
    
    // Count active accounts by customer
    @Query("SELECT COUNT(a) FROM Account a WHERE a.customerId = :customerId AND a.isActive = true")
    long countActiveAccountsByCustomerId(@Param("customerId") String customerId);
    
    // Find high balance accounts
    @Query("SELECT a FROM Account a WHERE a.balance > :threshold ORDER BY a.balance DESC")
    List<Account> findHighBalanceAccounts(@Param("threshold") BigDecimal threshold);
    
    // Check if account number exists
    boolean existsByAccountNumber(String accountNumber);
    
    // Find accounts by balance less than specified amount (low balance accounts)
    List<Account> findByBalanceLessThanAndIsActive(BigDecimal amount, boolean isActive);
}
