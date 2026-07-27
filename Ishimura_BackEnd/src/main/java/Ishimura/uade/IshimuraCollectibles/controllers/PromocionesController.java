package Ishimura.uade.IshimuraCollectibles.controllers;

import Ishimura.uade.IshimuraCollectibles.entity.Promocion;
import Ishimura.uade.IshimuraCollectibles.entity.dto.PromocionDTO;
import Ishimura.uade.IshimuraCollectibles.repository.PromocionRepository;
import Ishimura.uade.IshimuraCollectibles.service.PromocionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/promociones")
public class PromocionesController {

  private final PromocionRepository repo;
  private final PromocionService service;

  public PromocionesController(PromocionRepository repo, PromocionService service) {
    this.repo = repo;
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<PromocionDTO> crear(@RequestBody PromocionDTO p) {
    PromocionDTO saved = service.crear(p);
    return ResponseEntity.ok(saved);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PromocionDTO> actualizar(@PathVariable Long id, @RequestBody PromocionDTO body) {
    return ResponseEntity.ok(service.actualizar(id, body));
  }

  @GetMapping("/activas")
  public List<PromocionDTO> activas(@RequestParam Long coleccionableId) {
    return service.activas(coleccionableId);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    if (repo.existsById(id)) {
      repo.deleteById(id);
    }
    return ResponseEntity.noContent().build();
  }
}
