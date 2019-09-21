package com.wpspublish.customer;

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
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    @GetMapping
    Page<Customer> search(@SortDefault.SortDefaults({
            @SortDefault(sort = "firstName", direction = Sort.Direction.ASC)
    }) Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    ResponseEntity<Customer> findById(@PathVariable Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.isPresent() ? ResponseEntity.of(customer) : ResponseEntity.notFound().build();
    }

    @PostMapping
    ResponseEntity createCustomer(@Valid @RequestBody CustomerDto customerDto, HttpServletRequest request) {
        Customer newCust = customerService.createCustomer(customerDto);
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString())
                .path("/{id}")
                .buildAndExpand(newCust.getId());
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDto customerDto)
            throws CustomerBadRequestException {
        customerService.updateCustomer(id, customerDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCustomer(@PathVariable Long id) throws CustomerBadRequestException {
        customerService.deleteCustomer(id);
    }

}
