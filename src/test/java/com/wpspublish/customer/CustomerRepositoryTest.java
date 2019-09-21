package com.wpspublish.customer;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void returnsPage() {
        // database is initialized with script "data.sql"
        assertThat(
                customerRepository
                        .findAll(PageRequest.of(0, 10))
                        .getContent()
                        .size()
        ).isEqualTo(7);
    }

    @Test
    @Transactional
    public void idShouldBeReturnedWhenNewCustomerAdded() {
        // Arrange
        Customer customer = new Customer();
        customer.setAge(30);
        customer.setFirstName("John");
        customer.setLastName("Ripper");
        customer.setProfession("Actor");
        // Act
        customer = customerRepository.save(customer);
        // Assert
        assertThat(customer.getId()).isPositive();
    }

}
