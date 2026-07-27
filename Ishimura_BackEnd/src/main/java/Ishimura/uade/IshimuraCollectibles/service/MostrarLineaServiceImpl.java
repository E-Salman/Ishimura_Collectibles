package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.Linea;
import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarLineaDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.CLineaDTO;
import Ishimura.uade.IshimuraCollectibles.entity.Marca;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarLineaRepository;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarMarcaRepository;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarColeccionableRepository;
import Ishimura.uade.IshimuraCollectibles.exceptions.MarcaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MostrarLineaServiceImpl implements MostrarLineaService {
    @Autowired
    private MostrarLineaRepository mostrarLineaRepository;
    @Autowired
    private MostrarMarcaRepository mostrarMarcaRepository;
    @Autowired
    private MostrarColeccionableRepository coleccionableRepository;
    @Autowired
    private Ishimura.uade.IshimuraCollectibles.service.ColeccionableService coleccionableService;

    @Override
    public List<MostrarLineaDTO> listarLineas() {
        List<Linea> lineas = mostrarLineaRepository.findAll();
        return lineas.stream()
                .map(l -> new MostrarLineaDTO(l.getId(), l.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public MostrarLineaDTO crearLinea(CLineaDTO cLineaDTO) {
        Linea linea = new Linea();
        linea.setNombre(cLineaDTO.getNombre());
        if (cLineaDTO.getIdMarca() == null) {
            throw new Ishimura.uade.IshimuraCollectibles.exceptions.InvalidDomainStateException("idMarca es obligatorio");
        }
        // Verificar que la marca exista, lanzar 404 si no
        Marca marca = mostrarMarcaRepository.findById(cLineaDTO.getIdMarca())
                .orElseThrow(() -> new MarcaNotFoundException(cLineaDTO.getIdMarca()));
        linea.setMarca(marca);
        Linea saved = mostrarLineaRepository.save(linea);
        return new MostrarLineaDTO(saved.getId(), saved.getNombre());
    }

    @Override
    @Transactional
    public void borrarLinea(Long id) {
        Linea linea = mostrarLineaRepository.findById(id)
                .orElseThrow(() -> new Ishimura.uade.IshimuraCollectibles.exceptions.LineaNotFoundException(id));
        var coleccionables = coleccionableRepository.findByLinea_Id(id);
        for (var c : coleccionables) {
            coleccionableService.borrarColeccionable(c.getId());
        }
        mostrarLineaRepository.delete(linea);
    }
}
