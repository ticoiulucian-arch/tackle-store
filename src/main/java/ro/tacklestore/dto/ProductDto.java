package ro.tacklestore.dto;

import lombok.*;
import ro.tacklestore.model.enums.ProductType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductDto {
    // ...existing fields...
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;
    private List<String> imageUrls;
    private String brand;
    private ProductType type;
    private Long categoryId;
    private String categoryName;
    private boolean active;
    private Map<String, String> specifications;
    private Map<String, TranslationRequest> translations;
}

