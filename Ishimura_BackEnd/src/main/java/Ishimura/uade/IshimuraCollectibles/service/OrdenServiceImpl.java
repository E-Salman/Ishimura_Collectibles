package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.*;
import Ishimura.uade.IshimuraCollectibles.entity.dto.*;
import Ishimura.uade.IshimuraCollectibles.exceptions.*;
import Ishimura.uade.IshimuraCollectibles.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrdenServiceImpl implements OrdenService {

  private final OrdenRepository ordenRepository;
  private final UserRepository userRepository;
  private final MostrarColeccionableRepository coleccionableRepository;
  private final CatalogoRepository catalogoRepository;
  private final PricingService pricingService;

  public OrdenServiceImpl(
      OrdenRepository ordenRepository,
      UserRepository userRepository,
      MostrarColeccionableRepository coleccionableRepository,
      CatalogoRepository catalogoRepository,
      PricingService pricingService) {
    this.ordenRepository = ordenRepository;
    this.userRepository = userRepository;
    this.coleccionableRepository = coleccionableRepository;
    this.catalogoRepository = catalogoRepository;
    this.pricingService = pricingService;
  }

  // ===================== CREAR ORDEN =====================
  @Transactional
  @Override
  public OrdenDetalleDTO crearOrden(Long usuarioId, CrearOrdenDTO dto) {
    Usuario usuario = userRepository.findById(usuarioId)
        .orElseThrow(() -> new UserNotFoundException(usuarioId));
    boolean esAdmin = usuario.getRol() == Rol.ADMIN;

    Orden orden = new Orden();
    orden.setNumeroOrden(generarNumeroOrden());
    orden.setUsuario(usuario);
    orden.setMetodoPago(dto.getMetodoPago());
    orden.setCreadaEn(LocalDateTime.now());
    orden.setMontoTotal(BigDecimal.ZERO);

    // ===================== DIRECCIÓN DE ENVÍO =====================
    if (dto.getDireccionEnvio() != null) {
      DireccionEnvio dir = new DireccionEnvio();
      dir.setCalle(dto.getDireccionEnvio().getCalle());
      dir.setNumero(dto.getDireccionEnvio().getNumero());
      dir.setCiudad(dto.getDireccionEnvio().getCiudad());
      dir.setProvincia(dto.getDireccionEnvio().getProvincia());
      dir.setCodigoPostal(dto.getDireccionEnvio().getCodigoPostal());
      dir.setPais(dto.getDireccionEnvio().getPais());
      orden.setDireccionEnvio(dir);
    }

    // ===================== ITEMS Y STOCK =====================
    for (ItemDTO i : dto.getItems()) {
      var col = coleccionableRepository.findById(i.getColeccionableId())
          .orElseThrow(() -> new CollectibleNotFoundException(i.getColeccionableId()));
      if (!esAdmin && Boolean.FALSE.equals(col.getVisibilidad())) {
        throw new CollectibleNotVisibleException();
      }

      var catalogo = catalogoRepository.findById(i.getColeccionableId())
          .orElseThrow(() -> new CollectibleNotFoundException(i.getColeccionableId()));

      int stockActual = catalogo.getStock();
      int cantidad = i.getCantidad();

      if (stockActual < cantidad) {
        throw new InvalidDomainStateException("No hay stock suficiente para el producto: " + col.getNombre());
      }

      int nuevoStock = stockActual - cantidad;
      catalogoRepository.updateStock(i.getColeccionableId(), nuevoStock);

      var quote = pricingService.quoteForColeccionable(col, i.getCantidad(), null);
      BigDecimal precioUnit = quote.getPrecioEfectivo();
      BigDecimal subtotal = precioUnit.multiply(BigDecimal.valueOf(cantidad));

      OrdenItem item = new OrdenItem();
      item.setOrden(orden);
      item.setColeccionable(col);
      item.setCantidad(cantidad);
      item.setPrecioUnitario(precioUnit);
      item.setSubtotal(subtotal);

      orden.getArticulos().add(item);
    }

    // ===================== TOTAL =====================
    BigDecimal total = orden.getArticulos().stream()
        .map(OrdenItem::getSubtotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    orden.setMontoTotal(total);

    Orden guardada = ordenRepository.save(orden);
    return toDetalleDTO(guardada);
  }

  // ===================== LISTADOS =====================

  @Override
  public List<OrdenResumenDTO> listarResumenMias(Long usuarioId) {
    return ordenRepository.findAllByUsuario_Id(usuarioId).stream()
        .map(o -> new OrdenResumenDTO(o.getNumeroOrden(), o.getMontoTotal(), o.getCreadaEn()))
        .collect(Collectors.toList());
  }

  @Override
  public List<OrdenResumenDTO> listarResumenDeUsuario(Long usuarioId) {
    return listarResumenMias(usuarioId);
  }

  @Override
  public OrdenDetalleDTO detallePorNumero(String numeroOrden) {
    var orden = ordenRepository.findByNumeroOrden(numeroOrden)
        .orElseThrow(() -> new OrderNotFoundException(numeroOrden));
    return toDetalleDTO(orden);
  }

  @Override
  public List<OrdenDetalleDTO> listarMisOrdenesDetalle(Long usuarioId) {
    return ordenRepository.findAllByUsuarioIdDetailed(usuarioId)
        .stream().map(this::toOrdenDetalleDTOUser).toList();
  }

  @Override
  public List<OrdenDetalleDTO> listarTodasOrdenesDetalle() {
    return ordenRepository.findAllDetailed()
        .stream().map(this::toOrdenDetalleDTOAdmin).toList();
  }

  @Override
  public List<OrdenDetalleDTO> listarOrdenesDetallePorUsuario(Long usuarioId) {
    return ordenRepository.findAllByUsuarioIdDetailedAdmin(usuarioId)
        .stream().map(this::toOrdenDetalleDTOAdmin).toList();
  }

  // ===================== FILTROS =====================

  @Override
  public List<OrdenResumenDTO> listarPorMontoMenorA(BigDecimal max) {
    return ordenRepository.findByMontoTotalLessThanEqualOrderByCreadaEnDesc(max).stream()
        .map(o -> new OrdenResumenDTO(o.getNumeroOrden(), o.getMontoTotal(), o.getCreadaEn()))
        .toList();
  }

  @Override
  public List<OrdenResumenDTO> listarPorMontoMayorA(BigDecimal min) {
    return ordenRepository.findByMontoTotalGreaterThanEqualOrderByCreadaEnDesc(min).stream()
        .map(o -> new OrdenResumenDTO(o.getNumeroOrden(), o.getMontoTotal(), o.getCreadaEn()))
        .toList();
  }

  @Override
  public List<OrdenResumenDTO> listarAntesDe(LocalDateTime fecha) {
    return ordenRepository.findByCreadaEnBeforeOrderByCreadaEnDesc(fecha).stream()
        .map(o -> new OrdenResumenDTO(o.getNumeroOrden(), o.getMontoTotal(), o.getCreadaEn()))
        .toList();
  }

  @Override
  public List<OrdenResumenDTO> listarEnFecha(LocalDateTime desdeIncl, LocalDateTime hastaExcl) {
    return ordenRepository.findByCreadaEnBetweenOrderByCreadaEnDesc(desdeIncl, hastaExcl).stream()
        .map(o -> new OrdenResumenDTO(o.getNumeroOrden(), o.getMontoTotal(), o.getCreadaEn()))
        .toList();
  }

  @Override
  public List<OrdenResumenDTO> listarDespuesDe(LocalDateTime fecha) {
    return ordenRepository.findByCreadaEnAfterOrderByCreadaEnDesc(fecha).stream()
        .map(o -> new OrdenResumenDTO(o.getNumeroOrden(), o.getMontoTotal(), o.getCreadaEn()))
        .toList();
  }

  @Override
  public List<OrdenResumenDTO> listarPorMetodoPago(String metodo) {
    return ordenRepository.findByMetodoPagoIgnoreCaseOrderByCreadaEnDesc(metodo).stream()
        .map(o -> new OrdenResumenDTO(o.getNumeroOrden(), o.getMontoTotal(), o.getCreadaEn()))
        .toList();
  }

  @Override
  public List<OrdenResumenDTO> listarQueContienenColeccionable(Long coleccionableId) {
    return ordenRepository.findDistinctByArticulos_Coleccionable_IdOrderByCreadaEnDesc(coleccionableId).stream()
        .map(o -> new OrdenResumenDTO(o.getNumeroOrden(), o.getMontoTotal(), o.getCreadaEn()))
        .toList();
  }

  // ===================== HELPERS =====================

  private OrdenDetalleDTO toDetalleDTO(Orden o) {
    var items = o.getArticulos().stream().map(it -> new OrdenItemDTO(
        it.getColeccionable().getId(),
        it.getColeccionable().getNombre(),
        it.getCantidad(),
        it.getPrecioUnitario(),
        it.getSubtotal()
    )).toList();

    return new OrdenDetalleDTO(
        o.getNumeroOrden(),
        o.getMontoTotal(),
        o.getMetodoPago(),
        o.getCreadaEn(),
        items,
        o.getUsuario() != null ? o.getUsuario().getEmail() : null
    );
  }

  private String generarNumeroOrden() {
    return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }

  private OrdenDetalleDTO toOrdenDetalleDTOUser(Orden o) {
    var items = o.getArticulos().stream().map(this::toItemDTO).toList();
    var totalCalc = items.stream().map(OrdenItemDTO::getSubtotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    var total = (o.getMontoTotal() != null) ? o.getMontoTotal() : totalCalc;

    return new OrdenDetalleDTO(
        o.getNumeroOrden(),
        total,
        o.getMetodoPago(),
        o.getCreadaEn(),
        items,
        o.getUsuario() != null ? o.getUsuario().getEmail() : null
    );
  }

  private OrdenDetalleDTO toOrdenDetalleDTOAdmin(Orden o) {
    return toOrdenDetalleDTOUser(o);
  }

  private OrdenItemDTO toItemDTO(OrdenItem it) {
    var c = it.getColeccionable();
    var subtotal = it.getPrecioUnitario().multiply(BigDecimal.valueOf(it.getCantidad()));
    return new OrdenItemDTO(
        c.getId(),
        c.getNombre(),
        it.getCantidad(),
        it.getPrecioUnitario(),
        subtotal
    );
  }
}
