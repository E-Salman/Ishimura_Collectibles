package Ishimura.uade.IshimuraCollectibles.controllers;

import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarMarcaDTO;
import Ishimura.uade.IshimuraCollectibles.service.MostrarMarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/marcas")
public class MostrarMarcaController {
    @Autowired
    private MostrarMarcaService mostrarMarcaService;

    @GetMapping
    public List<MostrarMarcaDTO> listarMarcas() {
        return mostrarMarcaService.listarMarcas();
    }

    @PostMapping("/crear")
    public ResponseEntity<MostrarMarcaDTO> crearMarca(@RequestBody MostrarMarcaDTO dto) {
        MostrarMarcaDTO nuevaMarca = mostrarMarcaService.crearMarca(dto);
        return ResponseEntity.ok(nuevaMarca);
    }

    @DeleteMapping("/{marcaId}")
    public ResponseEntity<Void> borrarMarca(@PathVariable Long marcaId) {
        mostrarMarcaService.borrarMarca(marcaId);
        return ResponseEntity.noContent().build();
    }
}
