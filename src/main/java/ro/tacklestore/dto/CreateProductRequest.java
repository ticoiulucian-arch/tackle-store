package ro.tacklestore.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import ro.tacklestore.model.enums.ProductType;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateProductRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull @DecimalMin("0.01")
    private BigDecimal price;

    @NotNull @Min(0)
    private Integer stockQuantity;

    private String imageUrl;
    private String brand;

    @NotNull
    private ProductType type;

    private Long categoryId;
}

