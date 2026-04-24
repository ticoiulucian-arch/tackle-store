package ro.tacklestore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.tacklestore.dto.CategoryDto;
import ro.tacklestore.exception.ResourceNotFoundException;
import ro.tacklestore.mapper.CategoryMapper;
import ro.tacklestore.model.Category;
import ro.tacklestore.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    public List<CategoryDto> findAll() {
        return categoryRepository.findByParentIsNull().stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<CategoryDto> findAllFlat() {
        return categoryRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    public CategoryDto findById(Long id) {
        return categoryRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
    }

    @Transactional
    public CategoryDto create(CategoryDto dto) {
        var builder = Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .specTemplate(dto.getSpecTemplate() != null ? dto.getSpecTemplate() : List.of());
        if (dto.getParentId() != null) {
            var parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found: " + dto.getParentId()));
            builder.parent(parent);
        }
        return mapper.toDto(categoryRepository.save(builder.build()));
    }

    @Transactional
    public CategoryDto update(Long id, CategoryDto dto) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setImageUrl(dto.getImageUrl());
        category.setSpecTemplate(dto.getSpecTemplate() != null ? dto.getSpecTemplate() : List.of());
        if (dto.getParentId() != null) {
            var parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found: " + dto.getParentId()));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }
        return mapper.toDto(categoryRepository.save(category));
    }

    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
