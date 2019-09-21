package com.wpspublish.customer;

public interface CustomerService {

    Customer createCustomer(CustomerDto customerDto);

    void updateCustomer(Long id, CustomerDto customerDto) throws CustomerBadRequestException;

    void deleteCustomer(Long id) throws CustomerBadRequestException;
}
