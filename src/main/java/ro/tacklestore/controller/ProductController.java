package ro.tacklestore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ro.tacklestore.dto.CreateProductRequest;
import ro.tacklestore.dto.ProductDto;
import ro.tacklestore.dto.TranslationRequest;
import ro.tacklestore.model.enums.ProductType;
import ro.tacklestore.service.ProductService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Page<ProductDto> findAll(@PageableDefault(size = 20) Pageable pageable,
                                    @RequestHeader(value = "Accept-Language", required = false) String locale) {
        return productService.findAll(pageable, locale);
    }

    @GetMapping("/{id}")
    public ProductDto findById(@PathVariable Long id,
                               @RequestHeader(value = "Accept-Language", required = false) String locale) {
        return productService.findById(id, locale);
    }

    @GetMapping("/category/{categoryId}")
    public Page<ProductDto> findByCategory(@PathVariable Long categoryId,
                                           @PageableDefault(size = 20) Pageable pageable,
                                           @RequestHeader(value = "Accept-Language", required = false) String locale) {
        return productService.findByCategory(categoryId, pageable, locale);
    }

    @GetMapping("/type/{type}")
    public Page<ProductDto> findByType(@PathVariable ProductType type,
                                       @PageableDefault(size = 20) Pageable pageable,
                                       @RequestHeader(value = "Accept-Language", required = false) String locale) {
        return productService.findByType(type, pageable, locale);
    }

    @GetMapping("/search")
    public Page<ProductDto> search(@RequestParam String q,
                                   @PageableDefault(size = 20) Pageable pageable,
                                   @RequestHeader(value = "Accept-Language", required = false) String locale) {
        return productService.search(q, pageable, locale);
    }

    @GetMapping("/price-range")
    public Page<ProductDto> findByPriceRange(@RequestParam BigDecimal min,
                                              @RequestParam BigDecimal max,
                                              @PageableDefault(size = 20) Pageable pageable,
                                              @RequestHeader(value = "Accept-Language", required = false) String locale) {
        return productService.findByPriceRange(min, max, pageable, locale);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto create(@Valid @RequestBody CreateProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("/{id}")
    public ProductDto update(@PathVariable Long id, @Valid @RequestBody CreateProductRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @GetMapping("/filter")
    public Page<ProductDto> filter(@RequestParam(required = false) ProductType type,
                                   @RequestParam(required = false) Long categoryId,
                                   @RequestParam(required = false) String brand,
                                   @RequestParam Map<String, String> allParams,
                                   @PageableDefault(size = 20) Pageable pageable,
                                   @RequestHeader(value = "Accept-Language", required = false) String locale) {
        // Extract spec filters (everything except known params)
        Map<String, String> specs = new HashMap<>(allParams);
        specs.remove("type"); specs.remove("categoryId"); specs.remove("brand");
        specs.remove("page"); specs.remove("size"); specs.remove("sort");
        return productService.filter(type, categoryId, brand, specs, pageable, locale);
    }

    @GetMapping("/spec-options")
    public Map<String, List<String>> getSpecOptions(@RequestParam ProductType type) {
        return productService.getSpecOptions(type);
    }

    // Translation endpoints
    @GetMapping("/{id}/translations")
    public Map<String, TranslationRequest> getTranslations(@PathVariable Long id) {
        return productService.getTranslationsMap(id);
    }

    @PutMapping("/{id}/translations/{locale}")
    public TranslationRequest upsertTranslation(@PathVariable Long id,
                                                 @PathVariable String locale,
                                                 @RequestBody TranslationRequest request) {
        return productService.upsertTranslation(id, locale, request);
    }

    @DeleteMapping("/{id}/translations/{locale}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTranslation(@PathVariable Long id, @PathVariable String locale) {
        productService.deleteTranslation(id, locale);
    }
}
