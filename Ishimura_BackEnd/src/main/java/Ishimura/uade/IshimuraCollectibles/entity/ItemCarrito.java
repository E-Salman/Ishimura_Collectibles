package Ishimura.uade.IshimuraCollectibles.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "item_carrito",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_item_carrito_usuario_coleccionable",
            columnNames = {"usuario_id", "coleccionable_id"}
        )
    }
)
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coleccionable_id", nullable = false)
    private Coleccionable coleccionable;

    @Column(nullable = false)
    private Integer cantidad;
}
