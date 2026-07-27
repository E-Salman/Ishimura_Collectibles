package Ishimura.uade.IshimuraCollectibles.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.entity.ItemWishlist;
import Ishimura.uade.IshimuraCollectibles.entity.Usuario;
import Ishimura.uade.IshimuraCollectibles.entity.dto.WishlistItemDTO;
import Ishimura.uade.IshimuraCollectibles.exceptions.WishlistItemAlreadyExistsException;
import Ishimura.uade.IshimuraCollectibles.exceptions.CollectibleNotVisibleException;
import Ishimura.uade.IshimuraCollectibles.repository.ColeccionableRepository;
import Ishimura.uade.IshimuraCollectibles.repository.ItemWishlistRepository;

@Service
public class WishlistService {

    @Autowired
    private ItemWishlistRepository wishlistRepo;

    @Autowired
    private ColeccionableRepository coleccionableRepo;

    public List<WishlistItemDTO> obtenerWishlist(Long usuarioId) {
        List<ItemWishlist> items = wishlistRepo.findByUsuarioId(usuarioId);
        boolean incluirOcultos = esAdmin();

        return items.stream()
                .filter(i -> incluirOcultos || Boolean.TRUE.equals(i.getColeccionable().getVisibilidad()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public WishlistItemDTO agregarAWishlist(Usuario usuario, Long coleccionableId) {
        boolean existe = wishlistRepo.existsByUsuarioIdAndColeccionableId(usuario.getId(), coleccionableId);
        Coleccionable coleccionable = coleccionableRepo.findById(coleccionableId)
                .orElseThrow(() -> new RuntimeException("Coleccionable no encontrado"));
        if (!esAdmin() && Boolean.FALSE.equals(coleccionable.getVisibilidad())) {
            throw new CollectibleNotVisibleException();
        }
        if (existe) {
            throw new WishlistItemAlreadyExistsException(coleccionable.getNombre());
        }
        ItemWishlist item = new ItemWishlist();
        item.setUsuario(usuario);
        item.setColeccionable(coleccionable);

        return toDTO(wishlistRepo.save(item));
    }

    public void eliminarItem(Long id) {
        wishlistRepo.deleteById(id);
    }

    public void vaciarWishlist(Long usuarioId) {
        List<ItemWishlist> items = wishlistRepo.findByUsuarioId(usuarioId);
        wishlistRepo.deleteAll(items);
    }

    private WishlistItemDTO toDTO(ItemWishlist i) {
        Coleccionable c = i.getColeccionable();
        String imagen = (c.getImagenes() != null && !c.getImagenes().isEmpty())
                ? "http://localhost:4002/imagenes?id=" + c.getImagenes().get(0).getId()
                : null;

        return new WishlistItemDTO(
                i.getId(),
                c.getId(),
                c.getNombre(),
                c.getPrecio(),
                imagen);
    }

    private boolean esAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities() == null) return false;
        return auth.getAuthorities().stream().anyMatch(a -> "ADMIN".equals(a.getAuthority()));
    }
}
