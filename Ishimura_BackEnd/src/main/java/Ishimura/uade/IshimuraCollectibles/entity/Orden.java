package Ishimura.uade.IshimuraCollectibles.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orden")
public class Orden {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "numero_orden", unique = true, nullable = false, length = 20)
  private String numeroOrden;

  @ManyToOne(optional = false)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @Column(name = "monto_total", precision = 9, scale = 2, nullable = false)
  private java.math.BigDecimal montoTotal;

  @Column(name = "metodo_pago", nullable = false, length = 30)
  private String metodoPago;

  @Column(name = "creada_en", nullable = false)
  private java.time.LocalDateTime creadaEn = java.time.LocalDateTime.now();

  @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrdenItem> articulos = new ArrayList<>();

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "direccion_envio_id")
  private DireccionEnvio direccionEnvio;
}