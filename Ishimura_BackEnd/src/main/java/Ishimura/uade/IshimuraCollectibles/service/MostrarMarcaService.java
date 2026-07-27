package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarMarcaDTO;
import java.util.List;

public interface MostrarMarcaService {
    List<MostrarMarcaDTO> listarMarcas();
    MostrarMarcaDTO crearMarca(MostrarMarcaDTO dto); 
    void borrarMarca(Long id);
}
