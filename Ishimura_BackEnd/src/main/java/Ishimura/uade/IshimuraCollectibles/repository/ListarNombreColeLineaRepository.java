package Ishimura.uade.IshimuraCollectibles.repository;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.entity.dto.ListarColeLineaDTO;
import Ishimura.uade.IshimuraCollectibles.entity.dto.LineaResumenDTO;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface ListarNombreColeLineaRepository extends JpaRepository<Coleccionable, Long> {

        Optional<Coleccionable> findFirstByNombreIgnoreCase(String nombre);

        @Query("""
                        select new Ishimura.uade.IshimuraCollectibles.entity.dto.ListarColeLineaDTO(c.id, c.nombre)
                        from Linea l join l.coleccionables c
                        where l.id = :lineaId
                        and (:incluirOcultos = true or c.visibilidad = true)
                        """)
        List<ListarColeLineaDTO> listarColeccionablesPorLinea(@Param("lineaId") Long lineaId, @Param("incluirOcultos") boolean incluirOcultos);

        @Query("""
                        select new Ishimura.uade.IshimuraCollectibles.entity.dto.ListarColeLineaDTO(c.id, c.nombre)
                        from Linea l join l.coleccionables c
                        where l.marca.id = :marcaId
                        and (:incluirOcultos = true or c.visibilidad = true)
                        """)
        List<ListarColeLineaDTO> listarColeccionablesPorMarca(@Param("marcaId") Long marcaId, @Param("incluirOcultos") boolean incluirOcultos);

        @Query("""
                        select new Ishimura.uade.IshimuraCollectibles.entity.dto.ListarColeLineaDTO(c.id, c.nombre)
                        from Coleccionable c
                        where c.precio <= :precio
                        and (:incluirOcultos = true or c.visibilidad = true)
                        """)
        List<ListarColeLineaDTO> listarColeccionablesPorDebajoDe(@Param("precio") Double precio, @Param("incluirOcultos") boolean incluirOcultos);

        @Query("""
                        select new Ishimura.uade.IshimuraCollectibles.entity.dto.ListarColeLineaDTO(c.id, c.nombre)
                        from Coleccionable c
                        where c.precio >= :precio
                        and (:incluirOcultos = true or c.visibilidad = true)
                        """)
        List<ListarColeLineaDTO> listarColeccionablesPorEncimaDe(@Param("precio") Double precio, @Param("incluirOcultos") boolean incluirOcultos);

        @Query("select new Ishimura.uade.IshimuraCollectibles.entity.dto.LineaResumenDTO(l.id, l.nombre) from Linea l where l.marca.id = :marcaId")
        List<LineaResumenDTO> listarLineasPorMarca(@Param("marcaId") Long marcaId);
}
