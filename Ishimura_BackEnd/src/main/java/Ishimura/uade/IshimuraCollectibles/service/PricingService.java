package Ishimura.uade.IshimuraCollectibles.service;

import Ishimura.uade.IshimuraCollectibles.entity.Coleccionable;
import Ishimura.uade.IshimuraCollectibles.entity.Promocion;
import Ishimura.uade.IshimuraCollectibles.entity.PromotionType;
import Ishimura.uade.IshimuraCollectibles.entity.dto.PromocionDTO;
import Ishimura.uade.IshimuraCollectibles.repository.PromocionRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import Ishimura.uade.IshimuraCollectibles.service.PricingService;

@Service
public class PricingService {

  private final PromocionRepository promoRepo;

  public PricingService(PromocionRepository promoRepo) {
    this.promoRepo = promoRepo;
  }

  @Data
  @AllArgsConstructor
  public static class PriceQuote {
    private BigDecimal precioLista;
    private BigDecimal precioEfectivo;
    private BigDecimal descuentoUnit;
    private Long promocionId; // null si no aplica
    private String promocionTipo; // opcional
  }

  public PriceQuote quoteForColeccionable(Coleccionable c, int qty, String coupon) {
    BigDecimal lista = BigDecimal.valueOf(c.getPrecio()).setScale(2, RoundingMode.HALF_UP);
    List<Promocion> promos = promoRepo.findActiveForItem(c.getId(), LocalDateTime.now());

    BigDecimal bestDiscount = BigDecimal.ZERO;
    Promocion best = null;
    for (Promocion p : promos) {
      BigDecimal d;
      if (p.getTipo() == PromotionType.PERCENT) {
        d = lista.multiply(p.getValor()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
      } else { // FIXED
        d = p.getValor();
      }
      if (d.compareTo(bestDiscount) > 0) {
        bestDiscount = d;
        best = p;
      }
    }

    BigDecimal efectivo = lista.subtract(bestDiscount);
    if (efectivo.compareTo(BigDecimal.ZERO) < 0) efectivo = BigDecimal.ZERO;
    efectivo = efectivo.setScale(2, RoundingMode.HALF_UP);

    return new PriceQuote(lista, efectivo, bestDiscount, best != null ? best.getId() : null,
        best != null ? best.getTipo().name() : null);
  }
}

