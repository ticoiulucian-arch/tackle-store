package ro.tacklestore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.tacklestore.dto.CreateCustomerRequest;
import ro.tacklestore.dto.CustomerDto;
import ro.tacklestore.service.CustomerService;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{id}")
    public CustomerDto findById(@PathVariable Long id) {
        return customerService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto create(@Valid @RequestBody CreateCustomerRequest request) {
        return customerService.create(request);
    }

    @PutMapping("/{id}")
    public CustomerDto update(@PathVariable Long id, @Valid @RequestBody CreateCustomerRequest request) {
        return customerService.update(id, request);
    }
}

