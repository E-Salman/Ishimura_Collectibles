package Ishimura.uade.IshimuraCollectibles.entity.dto;

import lombok.Data;
import java.util.List;

@Data
public class CrearOrdenDTO {
  private String metodoPago;
  private List<ItemDTO> items;
  private DireccionEnvioDTO direccionEnvio;
}

