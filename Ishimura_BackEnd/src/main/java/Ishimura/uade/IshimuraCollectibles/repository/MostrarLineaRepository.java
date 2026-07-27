
package Ishimura.uade.IshimuraCollectibles.repository;

import Ishimura.uade.IshimuraCollectibles.entity.Linea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MostrarLineaRepository extends JpaRepository<Linea, Long> {
    boolean existsByMarca_Id(Long marcaId);
    java.util.List<Linea> findByMarca_Id(Long marcaId);
}// usar findall
