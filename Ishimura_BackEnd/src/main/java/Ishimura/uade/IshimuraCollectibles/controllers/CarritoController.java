package Ishimura.uade.IshimuraCollectibles.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;

import Ishimura.uade.IshimuraCollectibles.entity.ItemCarrito;
import Ishimura.uade.IshimuraCollectibles.entity.Usuario;
import Ishimura.uade.IshimuraCollectibles.entity.dto.CarritoItemDTO;
import Ishimura.uade.IshimuraCollectibles.service.CarritoService;

@RestController
@RequestMapping("/carrito")
@CrossOrigin(origins = "http://localhost:5173")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public ResponseEntity<List<CarritoItemDTO>> verCarrito() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        List<CarritoItemDTO> carrito = carritoService.obtenerCarrito(usuario.getId());
        return ResponseEntity.ok(carrito);
    }

    @PostMapping("/{coleccionableId}")
    public ResponseEntity<CarritoItemDTO> agregar(
            @PathVariable Long coleccionableId,
            @RequestParam(defaultValue = "1") int cantidad) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        CarritoItemDTO item = carritoService.agregarAlCarrito(usuario, coleccionableId, cantidad);
        return ResponseEntity.ok(item);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarCantidad(
            @PathVariable Long id,
            @RequestParam int cantidad) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();

        if (cantidad <= 0) {
            carritoService.eliminarItem(id);
            return ResponseEntity.noContent().build();
        }

        CarritoItemDTO actualizado = carritoService.actualizarCantidad(id, usuario.getId(), cantidad);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        carritoService.eliminarItem(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/vaciar")
    public ResponseEntity<Void> vaciar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();
        carritoService.vaciarCarrito(usuario.getId());
        return ResponseEntity.noContent().build();
    }
}



