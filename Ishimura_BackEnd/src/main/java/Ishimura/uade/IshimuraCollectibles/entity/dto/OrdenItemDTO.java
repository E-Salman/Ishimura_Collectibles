package Ishimura.uade.IshimuraCollectibles.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenItemDTO {
  private Long coleccionableId;
  private String nombre;
  private Integer cantidad;
  private BigDecimal precioUnitario;
  private BigDecimal subtotal;
}
