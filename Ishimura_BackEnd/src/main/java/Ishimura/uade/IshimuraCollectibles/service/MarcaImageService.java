package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.model.Imagen;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface MarcaImageService {
    Imagen create(Imagen image, Long idMarca);

    Imagen viewById(long id);

    List<Long> listIdsByMarca(Long idMarca);

    Imagen viewFirstByMarca(Long idMarca);

    Imagen replaceForMarca(Long idMarca, Long imageId, byte[] bytes);

    void deleteForMarca(Long idMarca, Long imageId);
}
