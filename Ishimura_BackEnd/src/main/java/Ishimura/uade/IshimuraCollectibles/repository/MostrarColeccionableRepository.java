package Ishimura.uade.IshimuraCollectibles.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;

@Repository
public interface MostrarColeccionableRepository extends JpaRepository<Coleccionable, Long> {

    @Query("select c from Marca m join m.lineas l join l.coleccionables c where c.id = :id")
    List<Coleccionable> findDetalleById(Long id);

    boolean existsByNombreIgnoreCaseAndLinea_Id(String nombre, Long lineaId);
    boolean existsByNombreIgnoreCaseAndLinea_IdAndIdNot(String nombre, Long lineaId, Long id);
    boolean existsByLinea_Id(Long lineaId);
    java.util.List<Coleccionable> findByLinea_Id(Long lineaId);

}
