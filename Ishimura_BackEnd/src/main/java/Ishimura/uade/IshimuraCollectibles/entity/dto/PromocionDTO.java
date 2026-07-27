package Ishimura.uade.IshimuraCollectibles.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import Ishimura.uade.IshimuraCollectibles.entity.PromotionScopeType;
import Ishimura.uade.IshimuraCollectibles.entity.PromotionType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromocionDTO {
    private PromotionType tipo;
    private BigDecimal valor;
    private PromotionScopeType scopeType;
    private Long scopeId;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private boolean activa;
    private int prioridad;
    private boolean stackable;

}
