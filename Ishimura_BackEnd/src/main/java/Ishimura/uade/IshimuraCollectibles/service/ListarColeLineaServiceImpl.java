package Ishimura.uade.IshimuraCollectibles.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import Ishimura.uade.IshimuraCollectibles.entity.dto.ListarColeLineaDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.LineaResumenDTO;
import Ishimura.uade.IshimuraCollectibles.repository.ListarNombreColeLineaRepository;

@Service
public class ListarColeLineaServiceImpl implements ListarColeLineaService {

    @Autowired
    private ListarNombreColeLineaRepository repo;

    @Override
    public List<ListarColeLineaDTO> coleccionablesPorLinea(Long lineaId) {
        boolean incluirOcultos = esAdmin();
        return repo.listarColeccionablesPorLinea(lineaId, incluirOcultos);
    }

    @Override
    public List<ListarColeLineaDTO> coleccionablesPorMarca(Long marcaId) {
        boolean incluirOcultos = esAdmin();
        return repo.listarColeccionablesPorMarca(marcaId, incluirOcultos);
    }

    @Override
    public List<ListarColeLineaDTO> coleccionablesPorDebajoDe(Double precio) {
        boolean incluirOcultos = esAdmin();
        return repo.listarColeccionablesPorDebajoDe(precio, incluirOcultos);
    }

    @Override
    public List<ListarColeLineaDTO> coleccionablesPorEncimaDe(Double precio) {
        boolean incluirOcultos = esAdmin();
        return repo.listarColeccionablesPorEncimaDe(precio, incluirOcultos);
    }

    @Override
    public List<LineaResumenDTO> lineasPorMarca(Long marcaId) {
        return repo.listarLineasPorMarca(marcaId);
    }

    private boolean esAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities() == null) return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> "ADMIN".equals(a.getAuthority()));
    }
}
