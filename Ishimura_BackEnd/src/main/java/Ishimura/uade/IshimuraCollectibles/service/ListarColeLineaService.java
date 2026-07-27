package Ishimura.uade.IshimuraCollectibles.service;

import java.util.List;
import Ishimura.uade.IshimuraCollectibles.entity.dto.ListarColeLineaDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.LineaResumenDTO;

public interface ListarColeLineaService {
    List<ListarColeLineaDTO> coleccionablesPorLinea(Long lineaId);

    List<ListarColeLineaDTO> coleccionablesPorMarca(Long marcaId);

    List<ListarColeLineaDTO> coleccionablesPorDebajoDe(Double precio);

    List<ListarColeLineaDTO> coleccionablesPorEncimaDe(Double precio);

    List<LineaResumenDTO> lineasPorMarca(Long marcaId);
}