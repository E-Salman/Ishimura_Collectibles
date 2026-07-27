package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarLineaDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.CLineaDTO;
import java.util.List;

public interface MostrarLineaService {
    List<MostrarLineaDTO> listarLineas();

    MostrarLineaDTO crearLinea(CLineaDTO cLineaDTO);

    void borrarLinea(Long id);
}
