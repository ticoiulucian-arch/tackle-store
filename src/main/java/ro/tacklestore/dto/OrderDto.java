package ro.tacklestore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.tacklestore.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private String orderNumber;
    private Long customerId;
    private String customerName;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String shippingCity;
    private String shippingPostalCode;
    private String shippingCountry;
    private LocalDateTime orderDate;
    private List<OrderItemDto> items;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemDto {
        private Long id;
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
    }
}

