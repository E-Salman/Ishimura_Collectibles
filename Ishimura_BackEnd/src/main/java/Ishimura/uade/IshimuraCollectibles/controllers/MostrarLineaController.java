package Ishimura.uade.IshimuraCollectibles.controllers;

import Ishimura.uade.IshimuraCollectibles.entity.dto.CLineaDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarLineaDTO;
import Ishimura.uade.IshimuraCollectibles.service.MostrarLineaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/lineas")
public class MostrarLineaController {
    @Autowired
    private MostrarLineaService mostrarLineaService;

    @GetMapping
    public List<MostrarLineaDTO> listarLineas() {
        return mostrarLineaService.listarLineas();
    }

    @PostMapping("/crear")
    public MostrarLineaDTO crearLinea(@Valid @RequestBody CLineaDTO lineaDTO) {
        return mostrarLineaService.crearLinea(lineaDTO);
    }

    @DeleteMapping("/{lineaId}")
    public void borrarLinea(@PathVariable Long lineaId) {
        mostrarLineaService.borrarLinea(lineaId);
    }
}
