package ro.tacklestore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.tacklestore.dto.CreateOrderRequest;
import ro.tacklestore.dto.OrderDto;
import ro.tacklestore.exception.ResourceNotFoundException;
import ro.tacklestore.mapper.OrderMapper;
import ro.tacklestore.model.Order;
import ro.tacklestore.model.OrderItem;
import ro.tacklestore.model.enums.OrderStatus;
import ro.tacklestore.repository.CustomerRepository;
import ro.tacklestore.repository.OrderRepository;
import ro.tacklestore.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMapper mapper;

    public Page<OrderDto> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(mapper::toDto);
    }

    public OrderDto findById(Long id) {
        return orderRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
    }

    public OrderDto findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderNumber));
    }

    public Page<OrderDto> findByCustomer(Long customerId, Pageable pageable) {
        return orderRepository.findByCustomerId(customerId, pageable).map(mapper::toDto);
    }

    @Transactional
    public OrderDto placeOrder(CreateOrderRequest req) {
        var customer = customerRepository.findById(req.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + req.getCustomerId()));

        var order = Order.builder()
                .orderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .customer(customer)
                .shippingAddress(req.getShippingAddress())
                .shippingCity(req.getShippingCity())
                .shippingPostalCode(req.getShippingPostalCode())
                .shippingCountry(req.getShippingCountry())
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (var itemReq : req.getItems()) {
            var product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemReq.getProductId()));

            if (product.getStockQuantity() < itemReq.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName()
                        + " (available: " + product.getStockQuantity() + ", requested: " + itemReq.getQuantity() + ")");
            }

            product.setStockQuantity(product.getStockQuantity() - itemReq.getQuantity());
            productRepository.save(product);

            var orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();

            order.addItem(orderItem);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));
        }

        order.setTotalAmount(total);
        return mapper.toDto(orderRepository.save(order));
    }

    @Transactional
    public OrderDto updateStatus(Long id, OrderStatus status) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot update a cancelled order");
        }

        if (status == OrderStatus.CANCELLED) {
            // Restore stock on cancellation
            for (var item : order.getItems()) {
                var product = item.getProduct();
                product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                productRepository.save(product);
            }
        }

        order.setStatus(status);
        return mapper.toDto(orderRepository.save(order));
    }
}

