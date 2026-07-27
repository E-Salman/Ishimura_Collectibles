package Ishimura.uade.IshimuraCollectibles.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;

public interface ColeccionableRepository extends JpaRepository<Coleccionable, Long> {
}