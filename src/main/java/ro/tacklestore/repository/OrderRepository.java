package ro.tacklestore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.tacklestore.model.Order;
import ro.tacklestore.model.enums.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
    Page<Order> findByCustomerId(Long customerId, Pageable pageable);
    List<Order> findByStatus(OrderStatus status);
}

