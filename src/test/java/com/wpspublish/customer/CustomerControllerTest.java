package com.wpspublish.customer;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CustomerController.class)
@WithMockUser(username = "user", password = "password", roles = "USER")
public class CustomerControllerTest {

    @Captor
    ArgumentCaptor<Pageable> pageableCaptor;

    @MockBean
    private CustomerServiceImpl customerService;

    @Autowired
    private MockMvc mockMvc;

    private static final Long CUSTOMER_ID = 100L;
    private static final String FIRST_NAME = "Talita";
    private static final String LAST_NAME = "Lima";
    private static final String PROFESSION = "Business Consultant";
    private static final int AGE = 35;
    private static final Customer CUSTOMER = new Customer(CUSTOMER_ID, FIRST_NAME, LAST_NAME, PROFESSION, AGE);
    private static final CustomerDto CUSTOMER_DTO =
            new CustomerDto(FIRST_NAME, LAST_NAME, PROFESSION, AGE, CUSTOMER.getDateCreated());

    @Test
    public void evaluatesPageableParameter() throws Exception {
        // Arrange and Act
        mockMvc.perform(get("/customer")
                .param("page", "5")
                .param("size", "10")
                .param("sort", "id,desc")   // <-- no space after comma!
                .param("sort", "first_name,asc")) // <-- no space after comma!
                .andExpect(status().isOk());

        // Assert
        verify(customerService).findAll(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        assertThat(pageable.getPageNumber(), is(5));
        assertThat(pageable.getPageSize(), is(10));
        Sort sort = pageable.getSort();
        assertThat(sort.getOrderFor("first_name").getDirection(), is(Direction.ASC));
        assertThat(sort.getOrderFor("id").getDirection(), is(Direction.DESC));
    }

    @Test
    public void findByIdShouldReturnOk() throws Exception {
        when(customerService.find(CUSTOMER_ID)).thenReturn(Optional.of(CUSTOMER_DTO));
        // Act and assert
        mockMvc.perform(get(String.format("/customer/%s", CUSTOMER_ID)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(FIRST_NAME)))
                .andExpect(content().string(containsString(LAST_NAME)))
                .andExpect(content().string(containsString(PROFESSION)))
                .andExpect(content().string(containsString(String.valueOf(AGE))));
        verify(customerService).find(CUSTOMER_ID);
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenCustomerDoestExist() throws Exception {
        when(customerService.find(CUSTOMER_ID)).thenReturn(Optional.empty());
        // Act and assert
        mockMvc.perform(get(String.format("/customer/%s", CUSTOMER_ID)))
                .andExpect(status().isNotFound());
        verify(customerService).find(CUSTOMER_ID);
    }

    @Test
    public void createCustomerShouldReturnLocationHeader() throws Exception {
        // Arrange
        when(customerService.createCustomer(any())).thenReturn(CUSTOMER);
        String payload = loadJson("customer-payload.json");
        // Act and assert
        mockMvc.perform(post("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("http://localhost/customer/100")));
        verify(customerService).createCustomer(any(CustomerDto.class));
    }

    @Test
    public void updateCustomerShouldReturnOk() throws Exception {
        // Arrange
        String payload = loadJson("customer-payload.json");
        // Act
        mockMvc.perform(put(String.format("/customer/%s", CUSTOMER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk());
        verify(customerService).updateCustomer(eq(CUSTOMER_ID), any(CustomerDto.class));
    }

    @Test
    public void updateCustomerShouldReturnBadRequestWhenCustomerDoesntExist() throws Exception {
        // Arrange
        doThrow(CustomerBadRequestException.class).when(customerService)
                .updateCustomer(eq(CUSTOMER_ID), any(CustomerDto.class));
        String payload = loadJson("customer-payload.json");
        // Act and assert
        mockMvc.perform(put(String.format("/customer/%s", CUSTOMER_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
        verify(customerService).updateCustomer(eq(CUSTOMER_ID), any(CustomerDto.class));
    }

    @Test
    public void deleteCustomerShouldReturnNoContent() throws Exception {
        // Arrange
        doThrow(CustomerBadRequestException.class).when(customerService).deleteCustomer(CUSTOMER_ID);
        // Act and assert
        mockMvc.perform(delete(String.format("/customer/%s", CUSTOMER_ID)))
                .andExpect(status().isBadRequest());
        verify(customerService).deleteCustomer(CUSTOMER_ID);
    }

    @Test
    public void deleteCustomerShouldReturnBadRequestWhenCustomerDoesntExist() throws Exception {
        // Arrange
        doThrow(CustomerBadRequestException.class).when(customerService)
                .deleteCustomer(CUSTOMER_ID);
        // Act and assert
        mockMvc.perform(delete(String.format("/customer/%s", CUSTOMER_ID)))
                .andExpect(status().isBadRequest());
        verify(customerService).deleteCustomer(CUSTOMER_ID);
    }

    private String loadJson(String file) throws Exception {
        Path path = Paths.get(getClass().getClassLoader().getResource(file).toURI());
        return Files.lines(path).collect(Collectors.joining("\n"));
    }
}
