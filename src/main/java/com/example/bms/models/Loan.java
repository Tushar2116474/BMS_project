package com.example.bms.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
public class Loan {
    
    public enum LoanType {
        PERSONAL_LOAN(15.0),
        CAR_LOAN(10.5),
        HOME_LOAN(9.0),
        GOLD_LOAN(12.0),
        EDUCATION_LOAN(9.5);
        
        private final double baseInterestRate;
        
        LoanType(double baseInterestRate) {
            this.baseInterestRate = baseInterestRate;
        }
        
        public double getBaseInterestRate() {
            return baseInterestRate;
        }
    }
    
    public enum LoanStatus {
        PENDING,
        APPROVED,
        REJECTED,
        DISBURSED,
        CLOSED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "loan_id", unique = true, nullable = false)
    private String loanId;
    
    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;
    
    @Column(name = "loan_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanType loanType;
    
    @Column(name = "offered_interest_rate", precision = 5, scale = 2)
    private BigDecimal offeredInterestRate;
    
    @Column(name = "customer_id", nullable = false)
    private String customerId;
    
    @Column(name = "loan_status")
    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus;
    
    @Column(name = "tenure_months")
    private Integer tenureMonths;
    
    @Column(name = "purpose")
    private String purpose;
    
    @Column(name = "monthly_emi", precision = 15, scale = 2)
    private BigDecimal monthlyEmi;
    
    @Column(name = "application_date")
    private LocalDateTime applicationDate;
    
    @Column(name = "approval_date")
    private LocalDateTime approvalDate;
    
    @Column(name = "disbursement_date")
    private LocalDateTime disbursementDate;
    
    @Column(name = "maturity_date")
    private LocalDateTime maturityDate;
    
    @Column(name = "outstanding_amount", precision = 15, scale = 2)
    private BigDecimal outstandingAmount;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Loan() {
        this.loanStatus = LoanStatus.PENDING;
        this.applicationDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Legacy method for backward compatibility
    public double getAmountAsDouble() {
        return amount != null ? amount.doubleValue() : 0.0;
    }
    
    public void setAmount(double amount) {
        this.amount = BigDecimal.valueOf(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
        this.updatedAt = LocalDateTime.now();
    }
    
    public BigDecimal getOfferedInterestRate() {
        return offeredInterestRate;
    }
    
    public void setOfferedInterestRate(BigDecimal offeredInterestRate) {
        this.offeredInterestRate = offeredInterestRate;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Legacy method for backward compatibility
    public double getOfferedInterestRateAsDouble() {
        return offeredInterestRate != null ? offeredInterestRate.doubleValue() : 0.0;
    }
    
    public void setOfferedInterestRate(double offeredInterestRate) {
        this.offeredInterestRate = BigDecimal.valueOf(offeredInterestRate);
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public LoanStatus getLoanStatus() {
        return loanStatus;
    }
    
    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
        this.updatedAt = LocalDateTime.now();
        
        // Set approval date when loan is approved
        if (loanStatus == LoanStatus.APPROVED && this.approvalDate == null) {
            this.approvalDate = LocalDateTime.now();
        }
        
        // Set disbursement date when loan is disbursed
        if (loanStatus == LoanStatus.DISBURSED && this.disbursementDate == null) {
            this.disbursementDate = LocalDateTime.now();
            this.outstandingAmount = this.amount; // Set outstanding amount to loan amount
        }
    }
    
    public Integer getTenureMonths() {
        return tenureMonths;
    }
    
    public void setTenureMonths(Integer tenureMonths) {
        this.tenureMonths = tenureMonths;
        this.updatedAt = LocalDateTime.now();
        
        // Calculate maturity date if disbursement date exists
        if (this.disbursementDate != null && tenureMonths != null) {
            this.maturityDate = this.disbursementDate.plusMonths(tenureMonths);
        }
    }
    
    public String getPurpose() {
        return purpose;
    }
    
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    
    public BigDecimal getMonthlyEmi() {
        return monthlyEmi;
    }
    
    public void setMonthlyEmi(BigDecimal monthlyEmi) {
        this.monthlyEmi = monthlyEmi;
    }
    
    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }
    
    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }
    
    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }
    
    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }
    
    public LocalDateTime getDisbursementDate() {
        return disbursementDate;
    }
    
    public void setDisbursementDate(LocalDateTime disbursementDate) {
        this.disbursementDate = disbursementDate;
    }
    
    public LocalDateTime getMaturityDate() {
        return maturityDate;
    }
    
    public void setMaturityDate(LocalDateTime maturityDate) {
        this.maturityDate = maturityDate;
    }
    
    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }
    
    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
        this.updatedAt = LocalDateTime.now();
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
    
    public double getBaseInterestRate() {
        return loanType != null ? loanType.getBaseInterestRate() : 0.0;
    }
    
    // Utility methods
    public BigDecimal calculateEmi() {
        if (amount != null && offeredInterestRate != null && tenureMonths != null && tenureMonths > 0) {
            double principal = amount.doubleValue();
            double monthlyRate = offeredInterestRate.doubleValue() / 12 / 100;
            
            if (monthlyRate == 0) {
                return BigDecimal.valueOf(principal / tenureMonths);
            }
            
            double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths)) / 
                        (Math.pow(1 + monthlyRate, tenureMonths) - 1);
            
            return BigDecimal.valueOf(emi).setScale(2, java.math.RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }
    
    public void updateEmi() {
        this.monthlyEmi = calculateEmi();
        this.updatedAt = LocalDateTime.now();
    }
}
