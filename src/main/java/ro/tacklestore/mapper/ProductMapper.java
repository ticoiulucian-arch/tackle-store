package ro.tacklestore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ro.tacklestore.dto.CreateProductRequest;
import ro.tacklestore.dto.ProductDto;
import ro.tacklestore.dto.TranslationRequest;
import ro.tacklestore.model.Product;
import ro.tacklestore.model.ProductSpecification;
import ro.tacklestore.model.ProductTranslation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "specifications", source = "specifications", qualifiedByName = "specsToMap")
    @Mapping(target = "translations", source = "translations", qualifiedByName = "translationsToMap")
    ProductDto toDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "specifications", ignore = true)
    @Mapping(target = "translations", ignore = true)
    Product toEntity(CreateProductRequest request);

    @Named("specsToMap")
    default Map<String, String> specsToMap(List<ProductSpecification> specs) {
        if (specs == null) return Collections.emptyMap();
        return specs.stream().collect(Collectors.toMap(ProductSpecification::getSpecKey, ProductSpecification::getSpecValue));
    }

    @Named("translationsToMap")
    default Map<String, TranslationRequest> translationsToMap(List<ProductTranslation> translations) {
        if (translations == null) return Collections.emptyMap();
        return translations.stream().collect(Collectors.toMap(
            ProductTranslation::getLocale,
            t -> TranslationRequest.builder().name(t.getName()).description(t.getDescription()).build(),
            (a, b) -> b,
            LinkedHashMap::new
        ));
    }
}
