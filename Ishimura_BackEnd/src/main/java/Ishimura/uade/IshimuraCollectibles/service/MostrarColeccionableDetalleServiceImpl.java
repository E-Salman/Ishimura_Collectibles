package Ishimura.uade.IshimuraCollectibles.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.entity.dto.ColeccionableNombreDTO;
import Ishimura.uade.IshimuraCollectibles.repository.ListarNombreColeLineaRepository;
import Ishimura.uade.IshimuraCollectibles.exceptions.CollectibleNotVisibleException;

@Service
public class MostrarColeccionableDetalleServiceImpl implements MostrarColeccionableNombreService {

    @Autowired
    private ListarNombreColeLineaRepository repo;

    @Override
    public ColeccionableNombreDTO detallePorNombre(String nombre) {
        Coleccionable c = repo.findFirstByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new Ishimura.uade.IshimuraCollectibles.exceptions.CollectibleNotFoundException("nombre=" + nombre));
        if (!esAdmin() && Boolean.FALSE.equals(c.getVisibilidad())) {
            throw new CollectibleNotVisibleException();
        }
        return new ColeccionableNombreDTO(c.getId(), c.getDescription(), c.getImagenes());
    }

    private boolean esAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities() == null) return false;
        return auth.getAuthorities().stream().anyMatch(a -> "ADMIN".equals(a.getAuthority()));
    }
}
