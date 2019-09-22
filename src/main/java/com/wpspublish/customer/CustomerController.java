package com.wpspublish.customer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customer")
@Api("Customer")
public class CustomerController {

    private final CustomerService customerService;

    @ApiOperation(value = "Searchs for customers in a paginable fashion")
    @GetMapping
    Page<CustomerDto> search(@SortDefault.SortDefaults({
            @SortDefault(sort = "firstName", direction = Sort.Direction.ASC)
    }) Pageable pageable) {
        return customerService.findAll(pageable);
    }

    @ApiOperation(value = "Finds a customer by the given id")
    @GetMapping("/{id}")
    ResponseEntity<CustomerDto> findById(@PathVariable Long id) {
        Optional<CustomerDto> customerDto = customerService.find(id);
        return customerDto.isPresent() ? ResponseEntity.of(customerDto) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Creates a new customer with the given information")
    @PostMapping
    ResponseEntity createCustomer(@Valid @RequestBody CustomerDto customerDto, HttpServletRequest request) {
        Customer newCust = customerService.createCustomer(customerDto);
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString())
                .path("/{id}")
                .buildAndExpand(newCust.getId());
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @ApiOperation(value = "Updates an existing customer with the given information")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDto customerDto)
            throws CustomerBadRequestException {
        customerService.updateCustomer(id, customerDto);
    }

    @ApiOperation(value = "Deletes an existing customer by the given id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCustomer(@PathVariable Long id) throws CustomerBadRequestException {
        customerService.deleteCustomer(id);
    }

}
