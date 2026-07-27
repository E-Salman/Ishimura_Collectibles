package Ishimura.uade.IshimuraCollectibles.repository;

import Ishimura.uade.IshimuraCollectibles.entity.Promocion;
import Ishimura.uade.IshimuraCollectibles.entity.PromotionScopeType;
import Ishimura.uade.IshimuraCollectibles.entity.dto.PromocionDTO;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Long> {

  @Query("""
    select p from Promocion p
    where p.activa = true
      and (p.inicio is null or p.inicio <= :now)
      and (p.fin is null or p.fin >= :now)
      and (
           p.scopeType = Ishimura.uade.IshimuraCollectibles.entity.PromotionScopeType.ALL
        or (p.scopeType = Ishimura.uade.IshimuraCollectibles.entity.PromotionScopeType.ITEM and p.scopeId = :coleId)
      )
    order by p.prioridad desc, p.id desc
  """)
  List<Promocion> findActiveForItem(@Param("coleId") Long coleId, @Param("now") LocalDateTime now);

  List<Promocion> findByScopeTypeAndScopeIdAndActiva(Ishimura.uade.IshimuraCollectibles.entity.PromotionScopeType scopeType,
                                                      Long scopeId,
                                                      boolean activa);
}
