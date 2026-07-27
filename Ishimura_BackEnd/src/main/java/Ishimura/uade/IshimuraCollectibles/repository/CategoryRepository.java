package Ishimura.uade.IshimuraCollectibles.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import Ishimura.uade.IshimuraCollectibles.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByDescription(String description);

    boolean existsByDescription(String description);
}
