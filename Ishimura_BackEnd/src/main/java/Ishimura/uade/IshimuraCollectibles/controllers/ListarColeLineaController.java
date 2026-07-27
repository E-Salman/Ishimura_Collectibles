package Ishimura.uade.IshimuraCollectibles.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Ishimura.uade.IshimuraCollectibles.entity.dto.ListarColeLineaDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.LineaResumenDTO;
import Ishimura.uade.IshimuraCollectibles.service.ListarColeLineaService;

@RestController
@RequestMapping("/listarColeLineas")
public class ListarColeLineaController {

    @Autowired
    private ListarColeLineaService service;

    // por línea
    @GetMapping("/coleccionables/linea/{lineaId}")
    public List<ListarColeLineaDTO> porLinea(@PathVariable Long lineaId) {
        return service.coleccionablesPorLinea(lineaId);
    }

    // por marca
    @GetMapping("/coleccionables/marca/{marcaId}")
    public List<ListarColeLineaDTO> porMarca(@PathVariable Long marcaId) {
        return service.coleccionablesPorMarca(marcaId);
    }

    // por debajo de x precio
    @GetMapping("/coleccionables/precio/menos/{precio}")
    public List<ListarColeLineaDTO> porDebajo(@PathVariable Double precio) {
        return service.coleccionablesPorDebajoDe(precio);
    }

    // por encima de x precio
    @GetMapping("/coleccionables/precio/mas/{precio}")
    public List<ListarColeLineaDTO> porEncima(@PathVariable Double precio) {
        return service.coleccionablesPorEncimaDe(precio);
    }

    // lineas de marca
    @GetMapping("/lineas/marca/{marcaId}")
    public List<LineaResumenDTO> lineasPorMarca(@PathVariable Long marcaId) {
        return service.lineasPorMarca(marcaId);
    }
}