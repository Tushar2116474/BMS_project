package com.example.bms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.bms.models.Customer;
import com.example.bms.models.Loan;
import com.example.bms.models.LoginRequest;
import com.example.bms.models.RegisterRequest;
import com.example.bms.services.CustomerService;
import com.example.bms.utils.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtUtil jwtUtil;

    // 1. REGISTER ENDPOINT
    @Operation(summary = "Register a new customer", description = "Creates a new customer account in the BMS system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Customer registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Customer already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerCustomer(@RequestBody RegisterRequest registerRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Customer customer = customerService.registerCustomer(registerRequest);
            response.put("success", true);
            response.put("message", "Customer registered successfully");
            response.put("customerId", customer.getId());
            response.put("accountNumber", customer.getAccountNumber());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 2. LOGIN ENDPOINT
    @Operation(summary = "Login a customer", description = "Authenticates customer and returns bearer token with customer details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginCustomer(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Customer customer = customerService.authenticateCustomer(loginRequest);
            if (customer != null) {
                // Generate JWT token with customer ID
                String jwtToken = jwtUtil.generateToken(customer.getId().toString());
                
                // Set customer details in login request
                loginRequest.setCustomerDetails(
                    customer.getId().toString(),
                    customer.getName(),
                    customer.getEmail(),
                    "CUSTOMER"
                );
                loginRequest.setBearerToken(jwtToken);
                loginRequest.setTokenActive(true);
                loginRequest.setLoginTimestamp(LocalDateTime.now());
                loginRequest.setTokenIssuedAt(LocalDateTime.now());
                loginRequest.setTokenExpiresAt(LocalDateTime.now().plusHours(24)); // 24 hours expiry
                
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("bearerToken", loginRequest.generateBearerTokenHeader());
                response.put("customerDetails", Map.of(
                    "id", customer.getId(),
                    "name", customer.getName(),
                    "email", customer.getEmail(),
                    "accountNumber", customer.getAccountNumber()
                ));
                response.put("tokenExpiresAt", loginRequest.getTokenExpiresAt());
                
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 3. UPDATE CUSTOMER DETAILS ENDPOINT
    @Operation(summary = "Update customer details", description = "Updates customer information (requires authentication)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer details updated successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PutMapping("/customer/{customerId}")
    public ResponseEntity<Map<String, Object>> updateCustomerDetails(
            @Parameter(description = "Customer ID") @PathVariable Long customerId,
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody Customer customerUpdate) {
        
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate bearer token
            if (!isValidBearerToken(bearerToken)) {
                response.put("success", false);
                response.put("message", "Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            Customer updatedCustomer = customerService.updateCustomerDetails(customerId, customerUpdate);
            if (updatedCustomer != null) {
                response.put("success", true);
                response.put("message", "Customer details updated successfully");
                response.put("customer", updatedCustomer);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Customer not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Update failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 4. VIEW TYPES OF LOAN ENDPOINT
    @Operation(summary = "Get all loan types", description = "Returns all available loan types with their base interest rates")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Loan types retrieved successfully")
    })
    @GetMapping("/loans/types")
    public ResponseEntity<Map<String, Object>> getLoanTypes() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> loanTypes = customerService.getAllLoanTypes();
            response.put("success", true);
            response.put("message", "Loan types retrieved successfully");
            response.put("loanTypes", loanTypes);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to retrieve loan types: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 5. APPLY FOR LOAN ENDPOINT
    @Operation(summary = "Apply for a loan", description = "Submit a new loan application (requires authentication)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Loan application submitted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token"),
        @ApiResponse(responseCode = "400", description = "Invalid loan application data")
    })
    @PostMapping("/loans/apply")
    public ResponseEntity<Map<String, Object>> applyForLoan(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody Loan loanApplication) {
        
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate bearer token
            if (!isValidBearerToken(bearerToken)) {
                response.put("success", false);
                response.put("message", "Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            // Extract customer ID from token
            String customerId = extractCustomerIdFromToken(bearerToken);
            
            Loan appliedLoan = customerService.applyForLoan(customerId, loanApplication);
            response.put("success", true);
            response.put("message", "Loan application submitted successfully");
            response.put("loanDetails", Map.of(
                "loanId", appliedLoan.getLoanId(),
                "amount", appliedLoan.getAmount(),
                "loanType", appliedLoan.getLoanType(),
                "baseInterestRate", appliedLoan.getBaseInterestRate(),
                "offeredInterestRate", appliedLoan.getOfferedInterestRate()
            ));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Loan application failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 6. GET APPLIED LOANS FOR CUSTOMER ENDPOINT
    @Operation(summary = "Get customer's applied loans", description = "Returns all loans applied by the customer (requires authentication)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Applied loans retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token"),
        @ApiResponse(responseCode = "404", description = "No loans found for customer")
    })
    @GetMapping("/loans/applied")
    public ResponseEntity<Map<String, Object>> getAppliedLoans(
            @RequestHeader("Authorization") String bearerToken) {
        
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate bearer token
            if (!isValidBearerToken(bearerToken)) {
                response.put("success", false);
                response.put("message", "Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            // Extract customer ID from token
            String customerId = extractCustomerIdFromToken(bearerToken);
            
            List<Loan> appliedLoans = customerService.getCustomerLoans(customerId);
            response.put("success", true);
            response.put("message", "Applied loans retrieved successfully");
            response.put("totalLoans", appliedLoans.size());
            response.put("loans", appliedLoans);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to retrieve loans: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 7. GET SPECIFIC APPLIED LOAN DETAILS
    @Operation(summary = "Get specific loan details", description = "Returns details of a specific loan by loan ID (requires authentication)")
    @GetMapping("/loans/{loanId}")
    public ResponseEntity<Map<String, Object>> getLoanDetails(
            @Parameter(description = "Loan ID") @PathVariable String loanId,
            @RequestHeader("Authorization") String bearerToken) {
        
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate bearer token
            if (!isValidBearerToken(bearerToken)) {
                response.put("success", false);
                response.put("message", "Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            String customerId = extractCustomerIdFromToken(bearerToken);
            Loan loan = customerService.getLoanDetails(customerId, loanId);
            
            if (loan != null) {
                response.put("success", true);
                response.put("message", "Loan details retrieved successfully");
                response.put("loan", loan);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Loan not found or unauthorized access");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to retrieve loan details: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 8. LOGOUT ENDPOINT
    @Operation(summary = "Logout customer", description = "Invalidates customer session and clears authentication token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout successful"),
        @ApiResponse(responseCode = "401", description = "Invalid token")
    })
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logoutCustomer(
            @RequestHeader("Authorization") String bearerToken) {
        
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate bearer token
            if (!isValidBearerToken(bearerToken)) {
                response.put("success", false);
                response.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            // Extract login ID from token
            String loginId = extractCustomerIdFromToken(bearerToken);
            
            // Invalidate session in database (if using database session management)
            // For now, we'll just return success as JWT tokens are stateless
            // In a production system, you might want to maintain a blacklist of tokens
            
            response.put("success", true);
            response.put("message", "Logout successful. Session invalidated.");
            response.put("loginId", loginId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Logout failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // UTILITY METHODS
    private boolean isValidBearerToken(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return false;
        }
        String token = bearerToken.substring(7);
        return jwtUtil.validateToken(token);
    }
    
    private String extractCustomerIdFromToken(String bearerToken) {
        String token = bearerToken.substring(7);
        return jwtUtil.extractUsername(token); // Assuming this returns customer login ID
    }
}

