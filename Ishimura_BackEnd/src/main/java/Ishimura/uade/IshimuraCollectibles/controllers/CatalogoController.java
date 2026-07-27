package Ishimura.uade.IshimuraCollectibles.controllers;

import Ishimura.uade.IshimuraCollectibles.entity.Catalogo;
import Ishimura.uade.IshimuraCollectibles.entity.dto.CatalogoDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.CatalogoListItemDTO;
import Ishimura.uade.IshimuraCollectibles.service.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("catalogo")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    // Listar todos los coleccionables en el catálogo
    @GetMapping
    public ResponseEntity<List<CatalogoListItemDTO>> getCatalogo() {
        return ResponseEntity.ok(catalogoService.getListado());
    }

    // obtener el coleccionable por id */
    /*@GetMapping("/{coleccionableId}")
    public ResponseEntity<Catalogo> getItem(@PathVariable Long coleccionableId) {
        return ResponseEntity.ok(catalogoService.stockProducto(coleccionableId));
    }*/

    @GetMapping("/{coleccionableId}")
    public ResponseEntity<CatalogoDTO> getItem(@PathVariable Long coleccionableId) {
        Catalogo catalogo = catalogoService.stockProducto(coleccionableId);

        CatalogoDTO dto = new CatalogoDTO(
            catalogo.getColeccionable().getId(),
            catalogo.getStock()
        );

        return ResponseEntity.ok(dto);
    }

    // cambiar stock a un valor específico
    @PutMapping("/{coleccionableId}/cambiarstock")
    public ResponseEntity<Void> cambiarStock(
            @PathVariable Long coleccionableId,
            @RequestParam int nuevoStock) {
        catalogoService.cambiarStock(coleccionableId, nuevoStock);
        return ResponseEntity.ok().build();
    }

    // incrementar el stock
    @PostMapping("/{coleccionableId}/incrementarstock")
    public ResponseEntity<Void> incrementarStock(
            @PathVariable Long coleccionableId,
            @RequestParam int cantidad) {
        catalogoService.incrementarStock(coleccionableId, cantidad);
        return ResponseEntity.ok().build();
    }

    // decrementar el stock
    @PostMapping("/{coleccionableId}/decrementarstock")
    public ResponseEntity<Void> decrementarStock(
            @PathVariable Long coleccionableId,
            @RequestParam int cantidad) {
        catalogoService.decrementarStock(coleccionableId, cantidad);
        return ResponseEntity.ok().build();
    }
}
