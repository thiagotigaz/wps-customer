package com.wpspublish.customer;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    Customer createCustomer(CustomerDto customerDto);

    void updateCustomer(Long id, CustomerDto customerDto) throws CustomerBadRequestException;

    void deleteCustomer(Long id) throws CustomerBadRequestException;

    Optional<CustomerDto> find(Long id);

    Page<Customer> findAll(Pageable pageable);
}
