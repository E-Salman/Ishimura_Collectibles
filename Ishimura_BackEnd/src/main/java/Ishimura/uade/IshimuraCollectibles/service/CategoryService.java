package Ishimura.uade.IshimuraCollectibles.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import Ishimura.uade.IshimuraCollectibles.entity.Category;
import Ishimura.uade.IshimuraCollectibles.exceptions.CategoryDuplicateException;

public interface CategoryService {
    public Page<Category> getCategories(PageRequest pageRequest);

    public Optional<Category> getCategoryById(Long categoryId);

    public Category createCategory(String description);
}