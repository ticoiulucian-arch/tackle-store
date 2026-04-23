package ro.tacklestore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_translations",
       uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "locale"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, length = 5)
    private String locale;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;
}

