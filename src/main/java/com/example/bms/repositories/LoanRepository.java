package com.example.bms.repositories;

import com.example.bms.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    
    // Find by loan ID (string)
    Optional<Loan> findByLoanId(String loanId);
    
    // Find loans by customer ID
    List<Loan> findByCustomerId(String customerId);
    
    // Find loans by loan type
    List<Loan> findByLoanType(Loan.LoanType loanType);
    
    // Find loans by amount range
    @Query("SELECT l FROM Loan l WHERE l.amount BETWEEN :minAmount AND :maxAmount")
    List<Loan> findByAmountRange(@Param("minAmount") double minAmount, @Param("maxAmount") double maxAmount);
    
    // Find loans by interest rate range
    @Query("SELECT l FROM Loan l WHERE l.offeredInterestRate BETWEEN :minRate AND :maxRate")
    List<Loan> findByInterestRateRange(@Param("minRate") double minRate, @Param("maxRate") double maxRate);
    
    // Find loans above certain amount
    List<Loan> findByAmountGreaterThan(double amount);
    
    // Find loans by customer and loan type
    List<Loan> findByCustomerIdAndLoanType(String customerId, Loan.LoanType loanType);
    
    // Count loans by customer
    @Query("SELECT COUNT(l) FROM Loan l WHERE l.customerId = :customerId")
    long countLoansByCustomerId(@Param("customerId") String customerId);
    
    // Get total loan amount by customer
    @Query("SELECT SUM(l.amount) FROM Loan l WHERE l.customerId = :customerId")
    Double getTotalLoanAmountByCustomerId(@Param("customerId") String customerId);
    
    // Find high-value loans (above specified amount)
    @Query("SELECT l FROM Loan l WHERE l.amount > :threshold ORDER BY l.amount DESC")
    List<Loan> findHighValueLoans(@Param("threshold") double threshold);
}
