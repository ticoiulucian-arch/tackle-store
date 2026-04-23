package ro.tacklestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tacklestore.model.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);
}

