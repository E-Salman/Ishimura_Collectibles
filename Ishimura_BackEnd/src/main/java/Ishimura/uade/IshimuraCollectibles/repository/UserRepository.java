package Ishimura.uade.IshimuraCollectibles.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Ishimura.uade.IshimuraCollectibles.entity.Rol;
import Ishimura.uade.IshimuraCollectibles.entity.Usuario;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByRol(Rol rol);

    @Query("select o.usuario from Orden o where o.id = :ordenId")
    Optional<Usuario> findUsuarioByOrdenId(@Param("ordenId") Long ordenId);
}

