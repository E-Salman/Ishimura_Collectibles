package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.model.Imagen;
import Ishimura.uade.IshimuraCollectibles.repository.ImageRepository;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarColeccionableRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Ishimura.uade.IshimuraCollectibles.exceptions.CollectibleNotFoundException;
import Ishimura.uade.IshimuraCollectibles.exceptions.ImageNotFoundException;
import Ishimura.uade.IshimuraCollectibles.exceptions.InvalidDomainStateException;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private MostrarColeccionableRepository coleccionableRepository;

    @Override
    public Imagen create(Imagen image, Long idCol) {
        Coleccionable coleccionable = coleccionableRepository.findById(idCol).orElseThrow(() -> new EntityNotFoundException("Coleccionable " + idCol + " no existe"));
        image.setColeccionable(coleccionable);
        image.setMarca(null);
        return imageRepository.save(image);
    }

    @Override
    public Imagen viewById(long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(id));
    }

    @Override
    public void deleteCollectibleImage(long id) {
        Imagen img = imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(id));
        // Asegurar que es una imagen de coleccionable (no de marca)
        if (img.getMarca() != null) {
            throw new InvalidDomainStateException("La imagen pertenece a una marca; use el endpoint de marcas");
        }
        imageRepository.delete(img);
    }

    @Override
    public void deleteFirstForColeccionable(long coleccionableId) {
        var list = imageRepository.findByColeccionableIdOrderByIdAsc(coleccionableId);
        if (list.isEmpty()) {
            throw new ImageNotFoundException(coleccionableId);
        }
        imageRepository.delete(list.get(0));
    }

    @Override
    public int deleteAllForColeccionable(long coleccionableId) {
        var list = imageRepository.findByColeccionableIdOrderByIdAsc(coleccionableId);
        if (list.isEmpty()) return 0;
        imageRepository.deleteAll(list);
        return list.size();
    }
}
