package Ishimura.uade.IshimuraCollectibles.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Ishimura.uade.IshimuraCollectibles.entity.dto.ColeccionableNombreDTO;
import Ishimura.uade.IshimuraCollectibles.service.MostrarColeccionableNombreService;

@RestController
@RequestMapping("/mostrar/coleccionable")
public class MostrarColeccionableNombreController {

    @Autowired
    private MostrarColeccionableNombreService service;

    // dado un nombre -> id, descripción e imagen
    @GetMapping("/detalle/{nombre}")
    public ColeccionableNombreDTO detalle(@PathVariable String nombre) {
        return service.detallePorNombre(nombre);
    }
}