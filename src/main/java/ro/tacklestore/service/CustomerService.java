package ro.tacklestore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.tacklestore.dto.CreateCustomerRequest;
import ro.tacklestore.dto.CustomerDto;
import ro.tacklestore.exception.ResourceNotFoundException;
import ro.tacklestore.mapper.CustomerMapper;
import ro.tacklestore.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public CustomerDto findById(Long id) {
        return customerRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    public CustomerDto findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + email));
    }

    @Transactional
    public CustomerDto create(CreateCustomerRequest req) {
        if (customerRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already in use: " + req.getEmail());
        }

        var customer = mapper.toEntity(req);
        return mapper.toDto(customerRepository.save(customer));
    }

    @Transactional
    public CustomerDto update(Long id, CreateCustomerRequest req) {
        var customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));

        customer.setFirstName(req.getFirstName());
        customer.setLastName(req.getLastName());
        customer.setPhone(req.getPhone());
        customer.setAddress(req.getAddress());
        customer.setCity(req.getCity());
        customer.setPostalCode(req.getPostalCode());
        customer.setCountry(req.getCountry());

        return mapper.toDto(customerRepository.save(customer));
    }
}
