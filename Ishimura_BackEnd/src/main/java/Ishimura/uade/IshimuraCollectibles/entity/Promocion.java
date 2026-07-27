package Ishimura.uade.IshimuraCollectibles.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promocion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promocion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 16)
  private PromotionType tipo; // PERCENT | FIXED

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal valor;   // % o importe

  @Enumerated(EnumType.STRING)
  @Column(name = "scope_type", nullable = false, length = 16)
  private PromotionScopeType scopeType; // ITEM|...

  @Column(name = "scope_id")
  private Long scopeId; // id de coleccionable / linea / marca

  @Column(name = "inicio")
  private LocalDateTime inicio; // null => ya

  @Column(name = "fin")
  private LocalDateTime fin; // null => sin fin

  @Column(nullable = false)
  private boolean activa = true;

  @Column(nullable = false)
  private int prioridad = 0; // mayor => se evalúa primero

  @Column(nullable = false)
  private boolean stackable = false; // reservado
}

