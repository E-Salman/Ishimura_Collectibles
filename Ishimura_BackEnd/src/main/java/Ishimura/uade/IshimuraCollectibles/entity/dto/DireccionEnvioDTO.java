package Ishimura.uade.IshimuraCollectibles.entity.dto;

import lombok.Data;

@Data
public class DireccionEnvioDTO {
    private String calle;
    private String numero;
    private String ciudad;
    private String provincia;
    private String codigoPostal;
    private String pais;
}