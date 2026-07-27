package Ishimura.uade.IshimuraCollectibles.controllers;

import Ishimura.uade.IshimuraCollectibles.entity.Usuario;
import Ishimura.uade.IshimuraCollectibles.entity.dto.OrdenDetalleDTO;
import Ishimura.uade.IshimuraCollectibles.service.OrdenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mis-compras")
@RequiredArgsConstructor
public class UsuarioVerComprasController {

    private final OrdenService ordenService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public List<OrdenDetalleDTO> misCompras(Authentication auth) {
        Long usuarioId = ((Usuario) auth.getPrincipal()).getId();
        return ordenService.listarMisOrdenesDetalle(usuarioId);
    }
}
