package Ishimura.uade.IshimuraCollectibles.controllers;

import Ishimura.uade.IshimuraCollectibles.entity.dto.OrdenDetalleDTO;
import Ishimura.uade.IshimuraCollectibles.service.OrdenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/compras")
@RequiredArgsConstructor
public class AdminVerComprasController {

    private final OrdenService ordenService;

    // Todas las compras
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<OrdenDetalleDTO> todas() {
        return ordenService.listarTodasOrdenesDetalle();
    }

    // Compras filtradas por usuario
    @GetMapping("/{usuarioId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<OrdenDetalleDTO> porUsuario(@PathVariable Long usuarioId) {
        return ordenService.listarOrdenesDetallePorUsuario(usuarioId);
    }
}