package ro.tacklestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tacklestore.model.ProductImage;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductIdOrderBySortOrderAsc(Long productId);
    void deleteByProductId(Long productId);
}

