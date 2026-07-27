package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.Marca;
import Ishimura.uade.IshimuraCollectibles.model.Imagen;
import Ishimura.uade.IshimuraCollectibles.repository.MarcaImageRepository;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarMarcaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Ishimura.uade.IshimuraCollectibles.exceptions.ImageNotFoundException;
import Ishimura.uade.IshimuraCollectibles.exceptions.MarcaNotFoundException;
import jakarta.transaction.Transactional;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

@Service
public class MarcaImageServiceImpl implements MarcaImageService {
    @Autowired
    private MarcaImageRepository marcaImageRepository;

    @Autowired
    private MostrarMarcaRepository marcaRepository;

    @Override
    public Imagen create(Imagen image, Long idMarca) {
        Marca marca = marcaRepository.findById(idMarca)
            .orElseThrow(() -> new MarcaNotFoundException(idMarca));

        // asegurar exclusividad: setear marca y limpiar coleccionable
        image.setMarca(marca);
        image.setColeccionable(null);
        return marcaImageRepository.save(image);
    }

    @Override
    public Imagen viewById(long id) {
        return marcaImageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(id));
    }

    @Override
    public java.util.List<Long> listIdsByMarca(Long idMarca) {
        // Asegura existencia de la marca para diferenciar 404 de "sin imágenes"
        if (!marcaRepository.existsById(idMarca)) {
            throw new MarcaNotFoundException(idMarca);
        }
        return marcaImageRepository.findByMarcaIdOrderByIdAsc(idMarca)
                .stream()
                .map(Imagen::getId)
                .map(Long::valueOf)
                .toList();
    }

    @Override
    public Imagen viewFirstByMarca(Long idMarca) {
        if (!marcaRepository.existsById(idMarca)) {
            throw new MarcaNotFoundException(idMarca);
        }
        return marcaImageRepository.findByMarcaIdOrderByIdAsc(idMarca)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ImageNotFoundException(idMarca));
    }

    @Override
    @Transactional
    public Imagen replaceForMarca(Long idMarca, Long imageId, byte[] bytes) {
        // asegurar que la marca existe (consistencia)
        if (!marcaRepository.existsById(idMarca)) {
            throw new MarcaNotFoundException(idMarca);
        }
        Imagen img = marcaImageRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException(imageId));
        // verificar pertenencia
        if (img.getMarca() == null || !idMarca.equals(img.getMarca().getId())) {
            throw new ImageNotFoundException(imageId);
        }
        try {
            img.setImage(new SerialBlob(bytes));
        } catch (java.sql.SQLException e) {
            throw new IllegalArgumentException("No se pudo procesar el archivo de imagen", e);
        }
        return marcaImageRepository.save(img);
    }

    @Override
    @Transactional
    public void deleteForMarca(Long idMarca, Long imageId) {
        if (!marcaRepository.existsById(idMarca)) {
            throw new MarcaNotFoundException(idMarca);
        }
        Imagen img = marcaImageRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException(imageId));
        if (img.getMarca() == null || !idMarca.equals(img.getMarca().getId())) {
            throw new ImageNotFoundException(imageId);
        }
        marcaImageRepository.delete(img);
    }
}
