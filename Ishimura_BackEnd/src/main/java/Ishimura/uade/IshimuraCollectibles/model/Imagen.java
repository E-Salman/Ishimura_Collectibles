package Ishimura.uade.IshimuraCollectibles.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Blob;

import com.fasterxml.jackson.annotation.JsonIgnore;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.entity.Marca;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "imagenes")
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    private Blob image;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "coleccionable_id", nullable = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Coleccionable coleccionable;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "marca_id", nullable = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Marca marca;

    @PrePersist
    @PreUpdate
    private void validateOwnershipExclusivity() {
        boolean hasCole = this.coleccionable != null;
        boolean hasMarca = this.marca != null;
        if (hasCole == hasMarca) { // both true or both false
            throw new IllegalArgumentException("La imagen debe pertenecer exactamente a una entidad: coleccionable o marca");
        }
    }
}
