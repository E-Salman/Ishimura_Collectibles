package Ishimura.uade.IshimuraCollectibles.repository;

import Ishimura.uade.IshimuraCollectibles.model.Imagen;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Imagen, Long> {
    List<Imagen> findByColeccionableIdOrderByIdAsc(Long coleccionableId);

    @Query("SELECT i.id FROM Imagen i WHERE i.coleccionable.id = :id ORDER BY i.id ASC LIMIT 1")
    Optional<Long> findTopByColeccionableIdOrderByIdAsc(@Param("id") Long coleId);
}
