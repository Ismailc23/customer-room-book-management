package com.rest.services;

import com.rest.Entity.CustomerEntity;
import com.rest.Entity.RoomEntity;
import com.rest.ExceptionHandling.CustomerExceptions.CustomerNotFoundException;
import com.rest.ExceptionHandling.CustomerExceptions.InvalidAgeException;
import com.rest.Repository.BookingRepository;
import com.rest.Repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public boolean isCustomerAbove18(CustomerEntity customer) {
        LocalDate current = LocalDate.now();
        LocalDate dateOfBirth = customer.getDateOfBirth();
        int age = Period.between(dateOfBirth, current).getYears();
        log.info("Age of the customer : {}", age );
        return age > 18;
    }

    public CustomerEntity createCustomer(CustomerEntity customer) {
        if(!isCustomerAbove18(customer)) {
            log.debug("Age is under 18");
            throw new InvalidAgeException("Customer must be above 18 years old");
        }
        customerRepository.save(customer);
        log.debug("Customer is saved Successfully : {}",customer);
        return customer;
    }

    public Optional<CustomerEntity> getCustomerById(Long id) {
        Optional<CustomerEntity> customer = customerRepository.findById(id);
        if(!customer.isPresent()) {
            throw new CustomerNotFoundException("Customer is not found with the given Id :"+id);
        }
        else {
            return customer;
        }
    }

    public List<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }

    public CustomerEntity updateCustomer(CustomerEntity customer) {
        Optional<CustomerEntity> existCustomer = customerRepository.findById(customer.getCustomerId());
        if (!existCustomer.isPresent()) {
            log.debug("Customer is not present");
            throw new CustomerNotFoundException("Customer is not found with the given Id :"+customer.getCustomerId());
        }
        if(!isCustomerAbove18(customer)) {
            log.debug("Age tryin to update is under 18");
            throw new InvalidAgeException("Customer must be above 18 years old");
        }
        customerRepository.save(customer);
        log.debug("Customer is updated Successfully : {}",customer);
        return customer;
    }

    public boolean deleteCustomer(Long id) {
        if(customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            log.debug("Deleted customer");
            return true;
        }
        else {
            log.debug("Customer is not found");
            throw new CustomerNotFoundException("Customer with id : "+id+" is not found to perform deletion");
        }
    }
}