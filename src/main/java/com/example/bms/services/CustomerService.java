package com.example.bms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bms.models.Customer;
import com.example.bms.models.Loan;
import com.example.bms.repositories.CustomerRepository;
import com.example.bms.repositories.LoanRepository;
import com.example.bms.models.LoginRequest;
import com.example.bms.models.RegisterRequest;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private LoanRepository loanRepository;

    public Customer registerCustomer(RegisterRequest registerRequest) {
        // Check if customer already exists
        Customer existingCustomer = customerRepository.findByLoginId(registerRequest.getLoginId());
        if (existingCustomer != null) {
            throw new RuntimeException("Customer with this login ID already exists");
        }
        
        Customer customer = new Customer();
        customer.setName(registerRequest.getName());
        customer.setAccountNumber(registerRequest.getAccountNumber());
        customer.setEmail(registerRequest.getEmail());
        customer.setPhoneNumber(registerRequest.getPhoneNumber());
        customer.setAddress(registerRequest.getAddress());
        customer.setState(registerRequest.getState());
        customer.setCountry(registerRequest.getCountry());
        customer.setPanNumber(registerRequest.getPanNumber());
        customer.setDob(registerRequest.getDob());
        customer.setAccountType(Customer.AccountType.valueOf(registerRequest.getAccountType().name()));
        customer.setLoginId(registerRequest.getLoginId());
        customer.setPassword(registerRequest.getPassword());
        return customerRepository.save(customer);
    }

    public Customer authenticateCustomer(LoginRequest loginRequest) {
        Customer customer = customerRepository.findByLoginId(loginRequest.getLoginId());
        if (customer != null && customer.getPassword().equals(loginRequest.getPassword())) {
            return customer;
        }
        return null;
    }
    
    public Customer updateCustomerDetails(Long customerId, Customer customerUpdate) {
        Optional<Customer> existingCustomerOpt = customerRepository.findById(customerId);
        if (existingCustomerOpt.isPresent()) {
            Customer existingCustomer = existingCustomerOpt.get();
            
            // Update only non-null fields
            if (customerUpdate.getName() != null) {
                existingCustomer.setName(customerUpdate.getName());
            }
            if (customerUpdate.getEmail() != null) {
                existingCustomer.setEmail(customerUpdate.getEmail());
            }
            if (customerUpdate.getPhoneNumber() != null) {
                existingCustomer.setPhoneNumber(customerUpdate.getPhoneNumber());
            }
            if (customerUpdate.getAddress() != null) {
                existingCustomer.setAddress(customerUpdate.getAddress());
            }
            // Note: Sensitive fields like account number, PAN, DOB should not be updated easily
            
            return customerRepository.save(existingCustomer);
        }
        return null;
    }
    
    public List<Map<String, Object>> getAllLoanTypes() {
        List<Map<String, Object>> loanTypes = new ArrayList<>();
        
        for (Loan.LoanType loanType : Loan.LoanType.values()) {
            Map<String, Object> loanTypeInfo = new HashMap<>();
            loanTypeInfo.put("type", loanType.name());
            loanTypeInfo.put("baseInterestRate", loanType.getBaseInterestRate());
            loanTypeInfo.put("description", getLoanTypeDescription(loanType));
            loanTypes.add(loanTypeInfo);
        }
        
        return loanTypes;
    }
    
    public Loan applyForLoan(String customerId, Loan loanApplication) {
        // Generate unique loan ID
        String loanId = "LOAN" + System.currentTimeMillis();
        loanApplication.setLoanId(loanId);
        
        // Set customer ID
        loanApplication.setCustomerId(customerId);
        
        // Set base interest rate from loan type
        if (loanApplication.getLoanType() != null) {
            // If no offered rate is set, use base rate
            if (loanApplication.getOfferedInterestRate() == null || 
                loanApplication.getOfferedInterestRate().compareTo(BigDecimal.ZERO) == 0) {
                loanApplication.setOfferedInterestRate(BigDecimal.valueOf(loanApplication.getLoanType().getBaseInterestRate()));
            }
        }
        
        // Calculate and set EMI
        loanApplication.updateEmi();
        
        // Save to database
        return loanRepository.save(loanApplication);
    }
    
    public List<Loan> getCustomerLoans(String customerId) {
        return loanRepository.findByCustomerId(customerId);
    }
    
    public Loan getLoanDetails(String customerId, String loanId) {
        Optional<Loan> loanOpt = loanRepository.findByLoanId(loanId);
        if (loanOpt.isPresent() && loanOpt.get().getCustomerId().equals(customerId)) {
            return loanOpt.get();
        }
        return null;
    }
    
    private String getLoanTypeDescription(Loan.LoanType loanType) {
        switch (loanType) {
            case PERSONAL_LOAN:
                return "Personal loans for individual financial needs";
            case CAR_LOAN:
                return "Loans for purchasing vehicles";
            case HOME_LOAN:
                return "Loans for purchasing or constructing homes";
            case GOLD_LOAN:
                return "Loans against gold collateral";
            case EDUCATION_LOAN:
                return "Loans for educational expenses";
            default:
                return "Standard loan product";
        }
    }
}
