package Ishimura.uade.IshimuraCollectibles.controllers;

import Ishimura.uade.IshimuraCollectibles.entity.Rol;
import Ishimura.uade.IshimuraCollectibles.entity.Usuario;
import Ishimura.uade.IshimuraCollectibles.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/filtrarPorEmail")
    public ResponseEntity<Usuario> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(usuarioService.getByEmail(email));
    }

    @GetMapping("/filtrarRol") // @RequestParam porque el dato viene en la query
    public ResponseEntity<List<Usuario>> getByRol(@RequestParam Rol rol) {
        return ResponseEntity.ok(usuarioService.getByRol(rol));
    }

    @GetMapping("/filtrarId/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        return usuarioService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}