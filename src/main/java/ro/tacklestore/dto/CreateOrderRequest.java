package ro.tacklestore.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateOrderRequest {

    @NotNull
    private Long customerId;

    @NotEmpty
    private List<OrderItemRequest> items;

    private String shippingAddress;
    private String shippingCity;
    private String shippingPostalCode;
    private String shippingCountry;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class OrderItemRequest {
        @NotNull
        private Long productId;

        @NotNull @Min(1)
        private Integer quantity;
    }
}

