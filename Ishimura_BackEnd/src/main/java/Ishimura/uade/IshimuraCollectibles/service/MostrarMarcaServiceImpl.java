package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.Marca;
import Ishimura.uade.IshimuraCollectibles.entity.Linea;
import Ishimura.uade.IshimuraCollectibles.entity.dto.MostrarMarcaDTO;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarMarcaRepository;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarLineaRepository;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarColeccionableRepository;
import Ishimura.uade.IshimuraCollectibles.repository.MarcaImageRepository;
import Ishimura.uade.IshimuraCollectibles.repository.MostrarLineaRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MostrarMarcaServiceImpl implements MostrarMarcaService {

    @Autowired
    private MostrarMarcaRepository mostrarMarcaRepository;
    @Autowired
    private MostrarLineaRepository mostrarLineaRepository;
    @Autowired
    private MarcaImageRepository marcaImageRepository;
    @Autowired
    private Ishimura.uade.IshimuraCollectibles.service.ColeccionableService coleccionableService;
    @Autowired
    private Ishimura.uade.IshimuraCollectibles.service.MostrarLineaService mostrarLineaService;
    @Override
    public List<MostrarMarcaDTO> listarMarcas() {
        return mostrarMarcaRepository.findAll().stream()
                .map(m -> new MostrarMarcaDTO(m.getId(), m.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MostrarMarcaDTO crearMarca(MostrarMarcaDTO dto) {
        Marca entity = new Marca();
        entity.setNombre(dto.getNombre().trim()); 
        Marca saved = mostrarMarcaRepository.save(entity);
        return new MostrarMarcaDTO(saved.getId(), saved.getNombre());
    }

    @Override
    @Transactional
    public void borrarMarca(Long id) {
        Marca marca = mostrarMarcaRepository.findById(id)
                .orElseThrow(() -> new Ishimura.uade.IshimuraCollectibles.exceptions.MarcaNotFoundException(id));

        // Borrar todas las líneas (y en cascada sus coleccionables) de la marca
        java.util.List<Linea> lineas = mostrarLineaRepository.findByMarca_Id(id);
        for (Linea l : lineas) {
            mostrarLineaService.borrarLinea(l.getId());
        }

        // Borrar imágenes asociadas a la marca
        var imgsMarca = marcaImageRepository.findByMarcaIdOrderByIdAsc(id);
        if (!imgsMarca.isEmpty()) {
            marcaImageRepository.deleteAll(imgsMarca);
        }

        // Finalmente borrar la marca
        mostrarMarcaRepository.delete(marca);
    }
}
    

