package ro.tacklestore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ro.tacklestore.dto.CategoryDto;
import ro.tacklestore.model.Category;
import ro.tacklestore.model.Product;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "productCount", source = "products", qualifiedByName = "productListSize")
    @Mapping(target = "parentId", source = "parent.id")
    CategoryDto toDto(Category category);

    @Named("productListSize")
    default int productListSize(List<Product> products) {
        return products != null ? products.size() : 0;
    }
}
