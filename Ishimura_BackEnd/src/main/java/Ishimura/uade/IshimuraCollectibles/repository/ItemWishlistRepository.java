package Ishimura.uade.IshimuraCollectibles.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import Ishimura.uade.IshimuraCollectibles.entity.ItemWishlist;

public interface ItemWishlistRepository extends JpaRepository<ItemWishlist, Long> {
    List<ItemWishlist> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioIdAndColeccionableId(Long usuarioId, Long coleccionableId);
    long deleteByColeccionableId(Long coleccionableId);
}
