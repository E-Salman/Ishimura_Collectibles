package Ishimura.uade.IshimuraCollectibles.repository;

import Ishimura.uade.IshimuraCollectibles.entity.Orden;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

  Optional<Orden> findByNumeroOrden(String numeroOrden);

  List<Orden> findAllByUsuario_Id(Long usuarioId);

  boolean existsByNumeroOrden(String numeroOrden);

  // filtro x monto
  List<Orden> findAllByMontoTotalLessThanEqual(BigDecimal monto);

  List<Orden> findAllByMontoTotalGreaterThanEqual(BigDecimal monto);

  // filtro x fecha
  List<Orden> findAllByCreadaEnBefore(LocalDateTime fecha);

  List<Orden> findAllByCreadaEn(LocalDateTime fecha); // misma fecha+hora exacta

  List<Orden> findAllByCreadaEnAfter(LocalDateTime fecha);

  // filtro x metodo de pago
  List<Orden> findAllByMetodoPago(String metodoPago);

  // —— SOLO ADMIN: ÓRDENES QUE CONTIENEN UN COLECCIONABLE ——
  @Query("select distinct o from Orden o join o.articulos a where a.coleccionable.id = :coleccionableId")
  List<Orden> findAllByColeccionableId(@Param("coleccionableId") Long coleccionableId);

  List<Orden> findByMontoTotalLessThanEqualOrderByCreadaEnDesc(BigDecimal max);

  List<Orden> findByMontoTotalGreaterThanEqualOrderByCreadaEnDesc(BigDecimal min);

  List<Orden> findByCreadaEnBeforeOrderByCreadaEnDesc(LocalDateTime fecha);

  List<Orden> findByCreadaEnAfterOrderByCreadaEnDesc(LocalDateTime fecha);

  List<Orden> findByCreadaEnBetweenOrderByCreadaEnDesc(LocalDateTime desdeIncl, LocalDateTime hastaExcl);

  List<Orden> findByMetodoPagoIgnoreCaseOrderByCreadaEnDesc(String metodo);

  List<Orden> findDistinctByArticulos_Coleccionable_IdOrderByCreadaEnDesc(Long coleccionableId);

  // —— CONSULTAS DETALLADAS ——
  @Query("select distinct o from Orden o left join fetch o.articulos oi left join fetch oi.coleccionable c left join fetch c.linea l where o.usuario.id = :usuarioId order by o.id desc")
  List<Orden> findAllByUsuarioIdDetailed(@Param("usuarioId") Long usuarioId);

  @Query("select distinct o from Orden o left join fetch o.articulos oi left join fetch oi.coleccionable c left join fetch c.linea l order by o.id desc")
  List<Orden> findAllDetailed();

  @Query("select distinct o from Orden o left join fetch o.articulos oi left join fetch oi.coleccionable c left join fetch c.linea l where o.usuario.id = :usuarioId order by o.id desc")
  List<Orden> findAllByUsuarioIdDetailedAdmin(@Param("usuarioId") Long usuarioId);
}
