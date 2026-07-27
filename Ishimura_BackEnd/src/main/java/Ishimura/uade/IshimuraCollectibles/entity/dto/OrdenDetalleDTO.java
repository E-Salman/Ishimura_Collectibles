package Ishimura.uade.IshimuraCollectibles.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenDetalleDTO {
  private String numeroOrden;
  private BigDecimal montoTotal;
  private String metodoPago;
  private LocalDateTime creadaEn;
  private List<OrdenItemDTO> items;
  private String emailUsuario;
}
