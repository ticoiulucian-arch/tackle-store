package ro.tacklestore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ro.tacklestore.model.Product;
import ro.tacklestore.model.enums.ProductType;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Page<Product> findByActiveTrue(Pageable pageable);
    Page<Product> findByCategoryIdAndActiveTrue(Long categoryId, Pageable pageable);
    Page<Product> findByTypeAndActiveTrue(ProductType type, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable);
    Page<Product> findByPriceBetweenAndActiveTrue(BigDecimal min, BigDecimal max, Pageable pageable);
    List<Product> findByBrandAndActiveTrue(String brand);
}

