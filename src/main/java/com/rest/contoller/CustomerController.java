package com.rest.contoller;

import com.rest.ExceptionHandling.CustomerExceptions.CustomerNotFoundException;
import com.rest.ExceptionHandling.CustomerExceptions.InvalidAgeException;
import com.rest.ExceptionHandling.CustomerExceptions.UserNameAlreadyExistException;
import com.rest.services.CustomerService;
import com.rest.Entity.CustomerEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/request/api/customer")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerEntity customer) {
        try {
            CustomerEntity createdCustomer = customerService.createCustomer(customer);
            log.debug("Customer is created");
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
        }
        catch (InvalidAgeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/request/api/customer/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id) {
        try {
            Optional<CustomerEntity> customer = customerService.getCustomerById(id);
            log.debug("Customer with ID : {} is present", id);
            return ResponseEntity.ok().body(customer);
        }
        catch (CustomerNotFoundException e) {
            log.error("Customer is not present with Id : {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/request/api/customer/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody CustomerEntity customer) {
        customer.setCustomerId(id);
        try {
            CustomerEntity updatedCustomer = customerService.updateCustomer(customer);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCustomer);
        }
        catch (UserNameAlreadyExistException | InvalidAgeException e) {
            log.error("Customer is not present with Id : {}", id);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(e.getMessage());
        }
    }

    @DeleteMapping("/request/api/customer/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        try {
            boolean deletedCustomer = customerService.deleteCustomer(id);
            return ResponseEntity.status(HttpStatus.OK).body("Customer deleted successfully");
        }
        catch (CustomerNotFoundException e) {
            log.error("Customer is not present with Id : {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}