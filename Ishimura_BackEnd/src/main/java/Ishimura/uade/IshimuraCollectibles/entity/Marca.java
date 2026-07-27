package Ishimura.uade.IshimuraCollectibles.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    public Marca() {
    }

    public Marca(String nombre) {
        this.nombre = nombre;
    }

    @OneToMany(mappedBy = "marca", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Linea> lineas = new ArrayList<>();
}
