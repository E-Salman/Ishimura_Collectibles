package Ishimura.uade.IshimuraCollectibles.controllers;

import Ishimura.uade.IshimuraCollectibles.model.Imagen;
import Ishimura.uade.IshimuraCollectibles.service.MarcaImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/marcasImages")
public class MarcaImagesController {

  @Autowired
  private MarcaImageService marcaImageService;

  // Lista los IDs de imágenes de una marca (vacía si no tiene)
  @GetMapping("/{marcaId}/imagenes")
  public ResponseEntity<List<Long>> list(@PathVariable Long marcaId) {
    return ResponseEntity.ok(marcaImageService.listIdsByMarca(marcaId));
  }

  // Devuelve la primera imagen de la marca. Por defecto, la entrega en PNG cuadrado (letterbox)
  @GetMapping("/{marcaId}/imagenes/primera")
  public ResponseEntity<byte[]> firstImage(
      @PathVariable Long marcaId,
      @RequestParam(name = "square", defaultValue = "true") boolean square,
      @RequestParam(name = "size", defaultValue = "256") int size,
      @RequestParam(name = "bg", defaultValue = "000000") String bg,
      @RequestParam(name = "pad", defaultValue = "0") int pad) throws SQLException, IOException {
    Imagen img = marcaImageService.viewFirstByMarca(marcaId);
    if (!square) {
      byte[] imageBytes = img.getImage().getBytes(1, (int) img.getImage().length());
      return ResponseEntity.ok().header("Content-Type", "image/png").body(imageBytes);
    }
    java.awt.Color color = Ishimura.uade.IshimuraCollectibles.util.ImageUtils.parseHexColor(bg, java.awt.Color.BLACK);
    try {
      byte[] png = Ishimura.uade.IshimuraCollectibles.util.ImageUtils.renderSquarePng(img.getImage(), size, color, pad);
      return ResponseEntity.ok().header("Content-Type", "image/png").body(png);
    } catch (Exception e) {
      // Fallback: devolver bytes originales si el render falla por formato
      byte[] imageBytes = img.getImage().getBytes(1, (int) img.getImage().length());
      return ResponseEntity.ok().header("Content-Type", "application/octet-stream").body(imageBytes);
    }
  }

  // Devuelve bytes por ID de imagen (independiente de la marca)
  @GetMapping("/imagenes/{imageId}")
  public ResponseEntity<byte[]> byId(@PathVariable Long imageId) throws SQLException, IOException {
    Imagen img = marcaImageService.viewById(imageId);
    byte[] imageBytes = img.getImage().getBytes(1, (int) img.getImage().length());
    return ResponseEntity.ok().header("Content-Type", "image/png").body(imageBytes);
  }

  // Devuelve la primera imagen de la marca ajustada a un cuadrado PNG (contain/letterbox)
  @GetMapping("/{marcaId}/imagenes/primera/square")
  public ResponseEntity<byte[]> firstSquare(
      @PathVariable Long marcaId,
      @RequestParam(name = "size", defaultValue = "256") int size,
      @RequestParam(name = "bg", defaultValue = "000000") String bg,
      @RequestParam(name = "pad", defaultValue = "0") int pad) throws SQLException, IOException {
    Imagen img = marcaImageService.viewFirstByMarca(marcaId);
    java.awt.Color color = Ishimura.uade.IshimuraCollectibles.util.ImageUtils.parseHexColor(bg, java.awt.Color.BLACK);
    byte[] png = Ishimura.uade.IshimuraCollectibles.util.ImageUtils.renderSquarePng(img.getImage(), size, color, pad);
    return ResponseEntity.ok().header("Content-Type", "image/png").body(png);
  }

  // Devuelve una imagen por id ajustada a cuadrado PNG (contain/letterbox)
  @GetMapping("/imagenes/{imageId}/square")
  public ResponseEntity<byte[]> byIdSquare(
      @PathVariable Long imageId,
      @RequestParam(name = "size", defaultValue = "256") int size,
      @RequestParam(name = "bg", defaultValue = "000000") String bg,
      @RequestParam(name = "pad", defaultValue = "0") int pad) throws SQLException, IOException {
    Imagen img = marcaImageService.viewById(imageId);
    java.awt.Color color = Ishimura.uade.IshimuraCollectibles.util.ImageUtils.parseHexColor(bg, java.awt.Color.BLACK);
    try {
      byte[] png = Ishimura.uade.IshimuraCollectibles.util.ImageUtils.renderSquarePng(img.getImage(), size, color, pad);
      return ResponseEntity.ok().header("Content-Type", "image/png").body(png);
    } catch (Exception e) {
      byte[] imageBytes = img.getImage().getBytes(1, (int) img.getImage().length());
      return ResponseEntity.ok().header("Content-Type", "application/octet-stream").body(imageBytes);
    }
  }

  // Sube una nueva imagen para la marca
  @PostMapping("/{marcaId}/imagenes")
  public ResponseEntity<Long> upload(@PathVariable Long marcaId, @RequestParam("file") MultipartFile file)
      throws IOException, SerialException, SQLException {
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("Archivo vacio");
    }
    Blob blob = new SerialBlob(file.getBytes());
    Imagen saved = marcaImageService.create(Imagen.builder().image(blob).build(), marcaId);
    return ResponseEntity.ok(saved.getId());
  }

  // Reemplazar el contenido de una imagen existente de la marca
  @PutMapping("/{marcaId}/imagenes/{imageId}")
  public ResponseEntity<Void> replace(@PathVariable Long marcaId,
                                      @PathVariable Long imageId,
                                      @RequestParam("file") MultipartFile file) throws IOException {
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("Archivo vacio");
    }
    marcaImageService.replaceForMarca(marcaId, imageId, file.getBytes());
    return ResponseEntity.noContent().build();
  }

  // Eliminar una imagen de la marca
  @DeleteMapping("/{marcaId}/imagenes/{imageId}")
  public ResponseEntity<Void> delete(@PathVariable Long marcaId, @PathVariable Long imageId) {
    marcaImageService.deleteForMarca(marcaId, imageId);
    return ResponseEntity.noContent().build();
  }
}
