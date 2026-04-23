package ro.tacklestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.tacklestore.model.ProductSpecification;
import ro.tacklestore.model.enums.ProductType;

import java.util.List;

public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, Long> {

    @Query("SELECT DISTINCT s.specKey FROM ProductSpecification s WHERE s.product.type = :type AND s.product.active = true")
    List<String> findDistinctKeysByProductType(ProductType type);

    @Query("SELECT DISTINCT s.specValue FROM ProductSpecification s WHERE s.specKey = :key AND s.product.type = :type AND s.product.active = true ORDER BY s.specValue")
    List<String> findDistinctValuesByKeyAndType(String key, ProductType type);
}

