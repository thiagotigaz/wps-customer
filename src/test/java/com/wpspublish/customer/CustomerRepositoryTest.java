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

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Ripper";
    private static final String PROFESSION = "Actor";
    private static final int AGE = 30;
    private static final Customer CUSTOMER = new Customer(null, FIRST_NAME, LAST_NAME, PROFESSION, AGE);

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
        // Act
        Customer persistedCustomer = customerRepository.save(CUSTOMER);
        // Assert
        assertThat(persistedCustomer.getId()).isPositive();
    }

}
