package com.rest.contoller;

import com.rest.services.BookingService;
import com.rest.services.CustomerService;
import com.rest.Entity.CustomerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);


    @PostMapping("/request/api/customer")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerEntity customer) {
        Optional<CustomerEntity> response = Optional.ofNullable(customerService.createCustomer(customer));
        if (response.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response.get());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not able to create");
        }
    }

    @GetMapping("/request/api/customer/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id) {
      if(customerService.getCustomerById(id).isPresent()){
            logger.debug("Customer is present : ");
            return ResponseEntity.ok().body(customerService.getCustomerById(id).get());
        }
      else {
          logger.debug("Customer is not present");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/request/api/customer/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id,@RequestBody CustomerEntity customer) {
        customer.setCustomerId(id);
        Optional<CustomerEntity> response = Optional.ofNullable(customerService.updateCustomer(customer));
        if (response.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(response.get());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not able to Update");
        }
    }

    @DeleteMapping("/request/api/customer/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        return customerService.deleteCustomer(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}