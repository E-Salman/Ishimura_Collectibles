package Ishimura.uade.IshimuraCollectibles.entity.dto;

import java.util.List;

import Ishimura.uade.IshimuraCollectibles.model.Imagen;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColeccionableNombreDTO {
    private Long id;
    private String descripcion;
    private List<Imagen> idImg;
}
