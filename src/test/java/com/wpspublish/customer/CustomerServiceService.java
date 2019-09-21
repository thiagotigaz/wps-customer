package com.wpspublish.customer;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceService {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerService customerService;

    // TODO add tests for service layer
}
