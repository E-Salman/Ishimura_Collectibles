package Ishimura.uade.IshimuraCollectibles.controllers;

import Ishimura.uade.IshimuraCollectibles.entity.Usuario;
import Ishimura.uade.IshimuraCollectibles.entity.dto.CrearOrdenDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.OrdenDetalleDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.OrdenResumenDTO;
import Ishimura.uade.IshimuraCollectibles.repository.UserRepository;
import Ishimura.uade.IshimuraCollectibles.service.OrdenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/ordenes")
@RequiredArgsConstructor
public class OrdenController {

  private final OrdenService ordenService;
  private final UserRepository userRepository;

  // Crear nueva orden para el usuario autenticado
  @PostMapping
  public ResponseEntity<OrdenDetalleDTO> crear(@RequestBody CrearOrdenDTO dto, Principal principal) {
    Long usuarioId = resolveUserId(principal);
    OrdenDetalleDTO nOrden = ordenService.crearOrden(usuarioId, dto);    
    return ResponseEntity.ok(nOrden);
  }

  // listar (resumen) mis órdenes: nro, monto, fecha
  @GetMapping("/mias")
  public ResponseEntity<List<OrdenResumenDTO>> mias(Principal principal) {
    Long usuarioId = resolveUserId(principal);    
    return ResponseEntity.ok(ordenService.listarResumenMias(usuarioId));
  }

  // listar (resumen) de un usuario dado (solo admin)
  @GetMapping("/usuario/{usuarioId}")
  public ResponseEntity<List<OrdenResumenDTO>> deUsuario(@PathVariable Long usuarioId) {
    return ResponseEntity.ok(ordenService.listarResumenDeUsuario(usuarioId));
  }

  // detalle por número de orden
  @GetMapping("/{numeroOrden}")
  public ResponseEntity<OrdenDetalleDTO> detalle(@PathVariable String numeroOrden) {
    return ResponseEntity.ok(ordenService.detallePorNumero(numeroOrden));
  }

  // Todas las órdenes con detalle – vista ADMIN
  @GetMapping("/admin/detalle")
  public ResponseEntity<List<OrdenDetalleDTO>> todasOrdenesDetalle() {
    return ResponseEntity.ok(ordenService.listarTodasOrdenesDetalle());
  }

// Órdenes con detalle de un usuario específico – vista ADMIN (por email)
@GetMapping("/admin/usuario/detalle")
public ResponseEntity<List<OrdenDetalleDTO>> ordenesDetallePorUsuario(@RequestParam String email) {
  Usuario usuario = userRepository.findByEmail(email)
      .orElseThrow(() -> new Ishimura.uade.IshimuraCollectibles.exceptions.UserNotFoundException(email));

  return ResponseEntity.ok(ordenService.listarOrdenesDetallePorUsuario(usuario.getId()));
}



  private Long resolveUserId(Principal principal) {
    String email = principal.getName();
    return userRepository.findByEmail(email)
        .map(Usuario::getId)
        .orElseThrow(() -> new Ishimura.uade.IshimuraCollectibles.exceptions.UserNotFoundException(email));
  }
}
