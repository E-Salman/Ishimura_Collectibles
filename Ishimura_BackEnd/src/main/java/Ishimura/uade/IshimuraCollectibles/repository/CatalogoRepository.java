package Ishimura.uade.IshimuraCollectibles.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import Ishimura.uade.IshimuraCollectibles.entity.Catalogo;
import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;

@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Long> {

    // cambiar stock con query
    @Modifying
    @Transactional
    @Query("UPDATE Catalogo c SET c.stock = :stock WHERE c.coleccionable.id = :coleccionableId")
    void updateStock(Long coleccionableId, Integer stock);
}
