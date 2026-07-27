package Ishimura.uade.IshimuraCollectibles.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import Ishimura.uade.IshimuraCollectibles.entity.Catalogo;
import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.entity.dto.CatalogoListItemDTO;
import Ishimura.uade.IshimuraCollectibles.repository.CatalogoRepository;
import Ishimura.uade.IshimuraCollectibles.repository.ImageRepository;
import Ishimura.uade.IshimuraCollectibles.exceptions.CatalogItemNotFoundException;
import Ishimura.uade.IshimuraCollectibles.exceptions.InvalidDomainStateException;
import Ishimura.uade.IshimuraCollectibles.exceptions.CollectibleNotVisibleException;

@Service
public class CatalogoService {

    private final CatalogoRepository repo;
    private final ImageRepository imageRepo;

    @Autowired
    public CatalogoService(CatalogoRepository repo, ImageRepository imageRepo) {
        this.repo = repo;
        this.imageRepo = imageRepo;
    }

    public List<Catalogo> getAll() {
        return repo.findAll();
    }

    public List<CatalogoListItemDTO> getListado() {
        boolean incluirOcultos = esAdmin();
        return repo.findAll().stream()
            .filter(item -> incluirOcultos || Boolean.TRUE.equals(item.getColeccionable().getVisibilidad()))
            .map(item -> {
            Coleccionable c = item.getColeccionable();
            Long firstImageId = imageRepo.findTopByColeccionableIdOrderByIdAsc(c.getId()).orElse(null);
            System.out.println("POST LONG");
            return new CatalogoListItemDTO(
                c.getId(),
                c.getNombre(),
                c.getPrecio(),
                item.getStock(),
                firstImageId
            );
        }).toList();
    }
    
    public Catalogo stockProducto(Long coleccionableId) {
        Catalogo item = repo.findById(coleccionableId)
                   .orElseThrow(() -> new CatalogItemNotFoundException(coleccionableId));
        if (!esAdmin() && Boolean.FALSE.equals(item.getColeccionable().getVisibilidad())) {
            throw new CollectibleNotVisibleException();
        }
        return item;
    }

    public void cambiarStock(Long coleccionableId, int nuevoStock) {
        Catalogo item = repo.findById(coleccionableId)
                            .orElseThrow(() -> new CatalogItemNotFoundException(coleccionableId));
        item.setStock(nuevoStock);
        repo.save(item);
    }

    public void incrementarStock(Long coleccionableId, int cantidadIncrementar) {
        if (cantidadIncrementar <= 0) {
            throw new IllegalArgumentException("La cantidad a incrementar debe ser mayor que 0");
        }
        Catalogo item = repo.findById(coleccionableId)
                            .orElseThrow(() -> new CatalogItemNotFoundException(coleccionableId));
        int stockActual = item.getStock();
        repo.updateStock(coleccionableId, cantidadIncrementar + stockActual);
        repo.save(item);
    }

    public void decrementarStock(Long coleccionableId, int cantidadIncrementar) {
        if (cantidadIncrementar <= 0) {
            throw new IllegalArgumentException("La cantidad a decrementar debe ser mayor que 0");
        }
        Catalogo item = repo.findById(coleccionableId)
                            .orElseThrow(() -> new CatalogItemNotFoundException(coleccionableId));
        int stockActual = item.getStock();
        int nuevo = stockActual - cantidadIncrementar;
        if (nuevo < 0) {
            throw new InvalidDomainStateException("No se puede decrementar el stock por debajo de 0");
        }
        repo.updateStock(coleccionableId, nuevo);
        repo.save(item);
    }

    private boolean esAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities() == null) return false;
        return auth.getAuthorities().stream().anyMatch(a -> "ADMIN".equals(a.getAuthority()));
    }
}
