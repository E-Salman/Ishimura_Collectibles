package Ishimura.uade.IshimuraCollectibles.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "orden_items")
public class OrdenItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // muchos items pertenecen a una misma orden
    @ManyToOne(optional = false)
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    // muchos items pueden tener el mismo id (el cliente lleva mas de 1)
    @ManyToOne(optional = false)
    @JoinColumn(name = "coleccionable_id", nullable = false)
    private Coleccionable coleccionable;

    // cuantos del mismo item?
    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 7, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @Column(precision = 9, scale = 2, nullable = false)
    private BigDecimal subtotal;
}
