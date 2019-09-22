package com.wpspublish.customer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceService {

    private static final Long CUSTOMER_ID = 1L;
    private static final String FIRST_NAME = "Thiago";
    private static final String LAST_NAME = "Lima";
    private static final String PROFESSION = "Software Architect";
    private static final int AGE = 29;
    private static final Customer CUSTOMER = new Customer(CUSTOMER_ID, FIRST_NAME, LAST_NAME, PROFESSION, AGE);
    private static final CustomerDto CUSTOMER_DTO = new CustomerDto(FIRST_NAME, LAST_NAME, PROFESSION, AGE);

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test(expected = CustomerBadRequestException.class)
    public void updateNonexistentCustomerShouldThrowException() throws Exception {
        // Arrange
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());
        // Act and assert
        customerService.updateCustomer(CUSTOMER_ID, CUSTOMER_DTO);
    }

    @Test
    public void updateExistentCustomerShouldCallRepoSave() throws Exception {
        // Arrange
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(CUSTOMER));
        // Act
        customerService.updateCustomer(CUSTOMER_ID, CUSTOMER_DTO);
        // Assert
        verify(customerRepository).findById(CUSTOMER_ID);
        verify(modelMapper).map(CUSTOMER_DTO, CUSTOMER);
        verify(customerRepository).save(CUSTOMER);
    }

    @Test(expected = CustomerBadRequestException.class)
    public void deleteNonexistentCustomerShouldThrowException() throws Exception {
        // Arrange
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());
        // Act and assert
        customerService.deleteCustomer(CUSTOMER_ID);
    }

    @Test
    public void deleteExistentCustomerShouldCallRepoDelete() throws Exception {
        // Arrange
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(CUSTOMER));
        // Act
        customerService.deleteCustomer(CUSTOMER_ID);
        // Assert
        verify(customerRepository).findById(CUSTOMER_ID);
        verify(customerRepository).deleteById(CUSTOMER_ID);
    }

    @Test
    public void createCustomerShouldPersist() {
        // Arrange
        when(modelMapper.map(CUSTOMER_DTO, Customer.class)).thenReturn(CUSTOMER);
        // Act
        customerService.createCustomer(CUSTOMER_DTO);
        // Assert
        verify(modelMapper).map(CUSTOMER_DTO, Customer.class);
        verify(customerRepository).save(CUSTOMER);
    }

}
