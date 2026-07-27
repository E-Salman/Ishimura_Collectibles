package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.dto.CrearOrdenDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.OrdenDetalleDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.OrdenResumenDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrdenService {

  // nueva orden de compra
  OrdenDetalleDTO crearOrden(Long usuarioId, CrearOrdenDTO dto);

  List<OrdenResumenDTO> listarResumenMias(Long usuarioId);

  List<OrdenResumenDTO> listarResumenDeUsuario(Long usuarioId);

  OrdenDetalleDTO detallePorNumero(String numeroOrden);

  List<OrdenResumenDTO> listarPorMontoMenorA(BigDecimal max);

  List<OrdenResumenDTO> listarPorMontoMayorA(BigDecimal min);

  List<OrdenResumenDTO> listarAntesDe(LocalDateTime fecha);

  List<OrdenResumenDTO> listarEnFecha(LocalDateTime desdeIncl, LocalDateTime hastaExcl);

  List<OrdenResumenDTO> listarDespuesDe(LocalDateTime fecha);

  List<OrdenResumenDTO> listarPorMetodoPago(String metodo);

  List<OrdenResumenDTO> listarQueContienenColeccionable(Long coleccionableId);

  List<OrdenDetalleDTO> listarMisOrdenesDetalle(Long usuarioId);

  List<OrdenDetalleDTO> listarTodasOrdenesDetalle();

  List<OrdenDetalleDTO> listarOrdenesDetallePorUsuario(Long usuarioId);
}
