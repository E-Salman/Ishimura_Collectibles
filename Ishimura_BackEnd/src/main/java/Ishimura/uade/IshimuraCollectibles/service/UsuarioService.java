
package Ishimura.uade.IshimuraCollectibles.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import Ishimura.uade.IshimuraCollectibles.entity.Rol;
import Ishimura.uade.IshimuraCollectibles.entity.Usuario;
import Ishimura.uade.IshimuraCollectibles.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UserRepository repo;

    public Usuario getByEmail(String email) {
        return repo.findByEmail(email)
            .orElseThrow(() -> new Ishimura.uade.IshimuraCollectibles.exceptions.UserNotFoundException(email));
    }

    public List<Usuario> getByRol(Rol rol) {
        return repo.findByRol(rol);
    }

    public Optional<Usuario> getById(Long id) {
        return repo.findById(id);
    }

}
