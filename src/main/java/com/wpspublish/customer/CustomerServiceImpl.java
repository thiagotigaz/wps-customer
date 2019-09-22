package com.wpspublish.customer;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    /**
     * Creates a customer with given dto information
     *
     * @param customerDto containing new customer information
     * @return new created customer
     */
    @Override
    @Transactional
    public Customer createCustomer(CustomerDto customerDto) {
        Customer newCust = modelMapper.map(customerDto, Customer.class);
        return customerRepository.save(newCust);
    }

    /**
     * Updates a customer with given id and dto information
     *
     * @param id of the customer
     * @param customerDto containing existing customer information
     * @throws CustomerBadRequestException when customer does not exist
     */
    @Override
    @Transactional
    public void updateCustomer(Long id, CustomerDto customerDto) throws CustomerBadRequestException {
        Optional<Customer> custOpt = customerRepository.findById(id);
        if (custOpt.isPresent()) {
            Customer managedCust = custOpt.get();
            modelMapper.map(customerDto, managedCust);
            customerRepository.save(managedCust);
        } else {
            throw new CustomerBadRequestException();
        }
    }

    /**
     * Deletes a customer by the given customer id
     *
     * @param id of the customer
     * @throws CustomerBadRequestException when customer does not exist
     */
    @Override
    @Transactional
    public void deleteCustomer(Long id) throws CustomerBadRequestException {
        Optional<Customer> custOpt = customerRepository.findById(id);
        if (custOpt.isPresent()) {
            customerRepository.deleteById(id);
        } else {
            throw new CustomerBadRequestException();
        }
    }

    /**
     * Finds a single customer and map to its dto before returning
     * @param id of the customer
     * @return customer dto containing restricted information
     */
    @Override
    public Optional<CustomerDto> find(Long id) {
        Optional<Customer> custOpt = customerRepository.findById(id);
        return custOpt.map(customer -> modelMapper.map(customer, CustomerDto.class));
    }

    /**
     * Finds all customers in a pageable fashion
     * @param pageable containing the information to be searched
     * @return pageable containing page information and list of customers
     */
    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }
}
