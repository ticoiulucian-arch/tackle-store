package ro.tacklestore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.tacklestore.dto.CreateOrderRequest;
import ro.tacklestore.dto.OrderDto;
import ro.tacklestore.model.enums.OrderStatus;
import ro.tacklestore.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public Page<OrderDto> findAll(@PageableDefault(size = 20) Pageable pageable) {
        return orderService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public OrderDto findById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @GetMapping("/number/{orderNumber}")
    public OrderDto findByOrderNumber(@PathVariable String orderNumber) {
        return orderService.findByOrderNumber(orderNumber);
    }

    @GetMapping("/customer/{customerId}")
    public Page<OrderDto> findByCustomer(@PathVariable Long customerId,
                                          @PageableDefault Pageable pageable) {
        return orderService.findByCustomer(customerId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto placeOrder(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.placeOrder(request);
    }

    @PutMapping("/{id}/status")
    public OrderDto updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return orderService.updateStatus(id, status);
    }
}

