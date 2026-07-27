package Ishimura.uade.IshimuraCollectibles.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.entity.dto.ColeccionableDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarColeccionableDTO;
import Ishimura.uade.IshimuraCollectibles.model.Imagen;
import Ishimura.uade.IshimuraCollectibles.service.ColeccionableService;
import Ishimura.uade.IshimuraCollectibles.service.ImageService;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/coleccionable")
public class MostrarColeccionableController {

    @Autowired
    private ColeccionableService coleccionableService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/{coleccionableID}")
    public MostrarColeccionableDTO mostrarAtributosID(@PathVariable Long coleccionableID) {
        return coleccionableService.mostrarAtributosID(coleccionableID);
    }

    @GetMapping("/{coleccionableID}/imagenes/{imagenID}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable Long coleccionableID, @PathVariable int imagenID) throws SQLException, IOException {
        MostrarColeccionableDTO coleccionable = coleccionableService.mostrarAtributosID(coleccionableID);
        if (coleccionable.getImagenes() == null || coleccionable.getImagenes().isEmpty()
                || imagenID < 0 || imagenID >= coleccionable.getImagenes().size()) {
            return ResponseEntity.notFound().build();
        }
        Imagen imagen = imageService.viewById(coleccionable.getImagenes().get(imagenID));
        byte[] imageBytes = imagen.getImage().getBytes(1, (int) imagen.getImage().length());
        return ResponseEntity.ok()
                .header("Content-Type", "image/png") // change to "image/jpeg" if it's a JPEG
                .body(imageBytes);
    }

    @PostMapping
    public ResponseEntity<Object> crearColeccionable(@RequestBody ColeccionableDTO coleccionableDTO) {
        Coleccionable nuevoColeccionable = coleccionableService.crearColeccionable(coleccionableDTO);
        return ResponseEntity.created(URI.create("/coleccionable/" + nuevoColeccionable.getId())).body(nuevoColeccionable);
    }

    @PutMapping("/{coleccionableID}")
    public ResponseEntity<Coleccionable> editarColeccionable(@PathVariable Long coleccionableID,
            @RequestBody ColeccionableDTO dto) {
        Coleccionable actualizado = coleccionableService.actualizarColeccionable(coleccionableID, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{coleccionableID}")
    public ResponseEntity<Void> borrarColeccionable(@PathVariable Long coleccionableID) {
        coleccionableService.borrarColeccionable(coleccionableID);
        return ResponseEntity.noContent().build();
    }
}