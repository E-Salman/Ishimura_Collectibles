package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.dto.ColeccionableNombreDTO;

public interface MostrarColeccionableNombreService {
    ColeccionableNombreDTO detallePorNombre(String nombre);
}
