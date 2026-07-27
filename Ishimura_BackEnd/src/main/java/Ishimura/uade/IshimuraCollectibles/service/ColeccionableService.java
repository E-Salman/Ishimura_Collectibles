package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.entity.dto.ColeccionableDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarColeccionableDTO;

public interface ColeccionableService {
    MostrarColeccionableDTO mostrarAtributosID(Long coleccionableId);

    Coleccionable crearColeccionable(ColeccionableDTO coleccionableDTO);

    Coleccionable actualizarColeccionable(Long id, ColeccionableDTO coleccionableDTO);

    void borrarColeccionable(Long id);
}
