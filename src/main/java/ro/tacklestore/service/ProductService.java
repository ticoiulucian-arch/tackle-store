package ro.tacklestore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.tacklestore.dto.CreateProductRequest;
import ro.tacklestore.dto.ProductDto;
import ro.tacklestore.dto.TranslationRequest;
import ro.tacklestore.exception.ResourceNotFoundException;
import ro.tacklestore.mapper.ProductMapper;
import ro.tacklestore.model.Product;
import ro.tacklestore.model.ProductSpecification;
import ro.tacklestore.model.ProductTranslation;
import ro.tacklestore.model.enums.ProductType;
import ro.tacklestore.repository.CategoryRepository;
import ro.tacklestore.repository.ProductRepository;
import ro.tacklestore.repository.ProductSpecificationRepository;
import ro.tacklestore.repository.ProductTranslationRepository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductSpecificationRepository specRepository;
    private final ProductTranslationRepository translationRepository;
    private final ProductMapper mapper;

    private ProductDto applyLocale(ProductDto dto, String locale) {
        if (locale == null || dto.getTranslations() == null) return dto;
        var tr = dto.getTranslations().get(locale);
        if (tr != null) {
            if (tr.getName() != null && !tr.getName().isBlank()) dto.setName(tr.getName());
            if (tr.getDescription() != null && !tr.getDescription().isBlank()) dto.setDescription(tr.getDescription());
        }
        return dto;
    }

    public Page<ProductDto> findAll(Pageable pageable, String locale) {
        return productRepository.findByActiveTrue(pageable).map(p -> applyLocale(mapper.toDto(p), locale));
    }

    public ProductDto findById(Long id, String locale) {
        return productRepository.findById(id)
                .map(p -> applyLocale(mapper.toDto(p), locale))
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    public Page<ProductDto> findByCategory(Long categoryId, Pageable pageable, String locale) {
        return productRepository.findByCategoryIdAndActiveTrue(categoryId, pageable).map(p -> applyLocale(mapper.toDto(p), locale));
    }

    public Page<ProductDto> findByType(ProductType type, Pageable pageable, String locale) {
        return productRepository.findByTypeAndActiveTrue(type, pageable).map(p -> applyLocale(mapper.toDto(p), locale));
    }

    public Page<ProductDto> search(String query, Pageable pageable, String locale) {
        return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(query, pageable).map(p -> applyLocale(mapper.toDto(p), locale));
    }

    public Page<ProductDto> findByPriceRange(BigDecimal min, BigDecimal max, Pageable pageable, String locale) {
        return productRepository.findByPriceBetweenAndActiveTrue(min, max, pageable).map(p -> applyLocale(mapper.toDto(p), locale));
    }

    @Transactional
    public ProductDto create(CreateProductRequest req) {
        var product = mapper.toEntity(req);

        if (req.getCategoryId() != null) {
            var category = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + req.getCategoryId()));
            product.setCategory(category);
        }

        return mapper.toDto(productRepository.save(product));
    }

    @Transactional
    public ProductDto update(Long id, CreateProductRequest req) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));

        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setStockQuantity(req.getStockQuantity());
        product.setImageUrl(req.getImageUrl());
        product.setBrand(req.getBrand());
        product.setType(req.getType());

        if (req.getCategoryId() != null) {
            var category = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + req.getCategoryId()));
            product.setCategory(category);
        }

        return mapper.toDto(productRepository.save(product));
    }

    @Transactional
    public void delete(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        product.setActive(false);
        productRepository.save(product);
    }

    public Page<ProductDto> filter(ProductType type, Long categoryId, String brand,
                                   Map<String, String> specs, Pageable pageable, String locale) {
        return productRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isTrue(root.get("active")));

            if (type != null) predicates.add(cb.equal(root.get("type"), type));
            if (categoryId != null) predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            if (brand != null) predicates.add(cb.equal(root.get("brand"), brand));

            for (var entry : specs.entrySet()) {
                Join<Product, ProductSpecification> specJoin = root.join("specifications");
                predicates.add(cb.and(
                    cb.equal(specJoin.get("specKey"), entry.getKey()),
                    cb.equal(specJoin.get("specValue"), entry.getValue())
                ));
            }

            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable).map(p -> applyLocale(mapper.toDto(p), locale));
    }

    // Translation CRUD
    public List<TranslationRequest> getTranslations(Long productId) {
        // returns all translations for a product – but we use the map from ProductDto
        return null; // handled via getTranslationsMap
    }

    public Map<String, TranslationRequest> getTranslationsMap(Long productId) {
        var translations = translationRepository.findByProductId(productId);
        Map<String, TranslationRequest> map = new LinkedHashMap<>();
        for (var t : translations) {
            map.put(t.getLocale(), TranslationRequest.builder().name(t.getName()).description(t.getDescription()).build());
        }
        return map;
    }

    @Transactional
    public TranslationRequest upsertTranslation(Long productId, String locale, TranslationRequest req) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
        var translation = translationRepository.findByProductIdAndLocale(productId, locale)
                .orElseGet(() -> {
                    var t = new ProductTranslation();
                    t.setProduct(product);
                    t.setLocale(locale);
                    return t;
                });
        translation.setName(req.getName());
        translation.setDescription(req.getDescription());
        translationRepository.save(translation);
        return TranslationRequest.builder().name(translation.getName()).description(translation.getDescription()).build();
    }

    @Transactional
    public void deleteTranslation(Long productId, String locale) {
        translationRepository.deleteByProductIdAndLocale(productId, locale);
    }

    public Map<String, List<String>> getSpecOptions(ProductType type) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (String key : specRepository.findDistinctKeysByProductType(type)) {
            result.put(key, specRepository.findDistinctValuesByKeyAndType(key, type));
        }
        return result;
    }
}
