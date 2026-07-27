package Ishimura.uade.IshimuraCollectibles.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
public class Linea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "marca_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Marca marca;

    @OneToMany(mappedBy = "linea", fetch = FetchType.LAZY)
    @JsonManagedReference("linea-coleccionables")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Coleccionable> coleccionables = new ArrayList<>();
}
