package Ishimura.uade.IshimuraCollectibles.repository;

import Ishimura.uade.IshimuraCollectibles.model.Imagen;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcaImageRepository extends JpaRepository<Imagen, Long> {
    List<Imagen> findByMarcaIdOrderByIdAsc(Long marcaId);

    @Query("""
            select i.id
            from Imagen i
            where i.marca.id = :marcaId
            order by i.id
            """)
    Optional<Long> findTopIdByMarcaId(@Param("marcaId") Long marcaId);
}