package Ishimura.uade.IshimuraCollectibles.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import Ishimura.uade.IshimuraCollectibles.entity.ItemCarrito;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    List<ItemCarrito> findByUsuarioId(Long usuarioId);
    Optional<ItemCarrito> findByUsuarioIdAndColeccionableId(Long usuarioId, Long coleccionableId);
    long deleteByColeccionableId(Long coleccionableId);
}
