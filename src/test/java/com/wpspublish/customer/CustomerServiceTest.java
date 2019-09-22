package com.wpspublish.customer;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    private static final Long CUSTOMER_ID = 1L;
    private static final String FIRST_NAME = "Thiago";
    private static final String LAST_NAME = "Lima";
    private static final String PROFESSION = "Software Architect";
    private static final int AGE = 29;
    private static final Customer CUSTOMER = new Customer(CUSTOMER_ID, FIRST_NAME, LAST_NAME, PROFESSION, AGE);
    private static final CustomerDto CUSTOMER_DTO =
            new CustomerDto(FIRST_NAME, LAST_NAME, PROFESSION, AGE, CUSTOMER.getDateCreated());

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

    @Test
    public void findShouldReturnDto() {
        // Arrange
        Optional<Customer> customerOptional = Optional.of(CUSTOMER);
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(customerOptional);
        when(modelMapper.map(CUSTOMER, CustomerDto.class)).thenReturn(CUSTOMER_DTO);
        // Act
        Optional<CustomerDto> customerDto = customerService.find(CUSTOMER_ID);
        // Assert
        verify(customerRepository).findById(CUSTOMER_ID);
        verify(modelMapper).map(CUSTOMER, CustomerDto.class);
        assertTrue(customerDto.isPresent());
    }

    @Test
    public void findAllShouldReturnPage() {
        // Arrange
        PageImpl page = new PageImpl(Arrays.asList(CUSTOMER, CUSTOMER, CUSTOMER));
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(customerRepository.findAll(pageRequest)).thenReturn(page);
        // Act
        Page<Customer> customerPage = customerService.findAll(pageRequest);
        // Assert
        verify(customerRepository).findAll(pageRequest);
        assertTrue(customerPage.hasContent());
        assertThat(customerPage.getContent().size(), is(3));
    }

}
