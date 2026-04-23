package ro.tacklestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tacklestore.model.ProductTranslation;

import java.util.List;
import java.util.Optional;

public interface ProductTranslationRepository extends JpaRepository<ProductTranslation, Long> {
    Optional<ProductTranslation> findByProductIdAndLocale(Long productId, String locale);
    List<ProductTranslation> findByProductId(Long productId);
    void deleteByProductIdAndLocale(Long productId, String locale);
}

