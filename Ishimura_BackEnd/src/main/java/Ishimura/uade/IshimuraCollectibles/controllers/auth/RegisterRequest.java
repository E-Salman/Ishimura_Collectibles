package Ishimura.uade.IshimuraCollectibles.controllers.auth;

import Ishimura.uade.IshimuraCollectibles.entity.Rol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String direccion;
    private Rol rol;
}
