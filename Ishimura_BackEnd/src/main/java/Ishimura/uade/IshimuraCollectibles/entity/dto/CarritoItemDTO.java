package Ishimura.uade.IshimuraCollectibles.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoItemDTO {
    private Long id;
    private Long coleccionableId;
    private String nombre;
    private Double precio;
    private Integer cantidad;
    private String imagenUrl;
}

