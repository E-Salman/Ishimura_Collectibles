package Ishimura.uade.IshimuraCollectibles.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import Ishimura.uade.IshimuraCollectibles.entity.Category;
import Ishimura.uade.IshimuraCollectibles.exceptions.CategoryDuplicateException;
import Ishimura.uade.IshimuraCollectibles.repository.CategoryRepository;
import jakarta.transaction.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

  @Autowired
  private CategoryRepository categoryRepository;

  public Page<Category> getCategories(PageRequest pageable) {
    return categoryRepository.findAll(pageable);
  }

  public Optional<Category> getCategoryById(Long categoryId) {
    return categoryRepository.findById(categoryId);
  }

  @Transactional
  public Category createCategory(String description) {
    if (categoryRepository.existsByDescription(description)) {
      throw new CategoryDuplicateException(description);
    }
    try {
      return categoryRepository.save(new Category(description));
    } catch (DataIntegrityViolationException e) {
      throw new CategoryDuplicateException(description);
    }
  }
}
