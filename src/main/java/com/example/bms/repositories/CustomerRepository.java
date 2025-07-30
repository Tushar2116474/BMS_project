
package com.example.bms.repositories;

import com.example.bms.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Find by login ID
    Customer findByLoginId(String loginId);
    
    // Find by email
    Optional<Customer> findByEmail(String email);
    
    // Find by account number
    Optional<Customer> findByAccountNumber(String accountNumber);
    
    // Find by PAN number
    Optional<Customer> findByPanNumber(String panNumber);
    
    // Find by phone number
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    
    // Find customers by name (case insensitive)
    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Customer> findByNameContainingIgnoreCase(@Param("name") String name);
    
    // Check if login ID exists
    boolean existsByLoginId(String loginId);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Check if account number exists
    boolean existsByAccountNumber(String accountNumber);
    
    // Check if PAN number exists
    boolean existsByPanNumber(String panNumber);
    
    // Find customers by partial email domain
    @Query("SELECT c FROM Customer c WHERE c.email LIKE %:domain%")
    List<Customer> findByEmailDomain(@Param("domain") String domain);
    
    // Find customers registered after a certain date
    @Query("SELECT c FROM Customer c WHERE c.id > :customerId")
    List<Customer> findCustomersRegisteredAfter(@Param("customerId") Long customerId);
    
    // Search customers by multiple criteria
    @Query("SELECT c FROM Customer c WHERE " +
           "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:phoneNumber IS NULL OR c.phoneNumber LIKE CONCAT('%', :phoneNumber, '%'))")
    List<Customer> searchCustomers(@Param("name") String name, 
                                  @Param("email") String email, 
                                  @Param("phoneNumber") String phoneNumber);
}
