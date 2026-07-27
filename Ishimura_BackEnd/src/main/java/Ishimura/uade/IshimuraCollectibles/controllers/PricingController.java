package Ishimura.uade.IshimuraCollectibles.controllers;

import Ishimura.uade.IshimuraCollectibles.repository.MostrarColeccionableRepository;
import Ishimura.uade.IshimuraCollectibles.service.PricingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/precio")
public class PricingController {

  private final MostrarColeccionableRepository coleRepo;
  private final PricingService pricingService;

  public PricingController(MostrarColeccionableRepository coleRepo, PricingService pricingService) {
    System.out.println(">>> Controller created by Spring. pricingService = " + pricingService);
    this.coleRepo = coleRepo;
    this.pricingService = pricingService;
  }

  @GetMapping("/preview")
  public ResponseEntity<PricingService.PriceQuote> preview(@RequestParam Long coleccionableId,
                                                           @RequestParam(defaultValue = "1") int qty) {
    var col = coleRepo.findById(coleccionableId).orElseThrow();
    var quote = pricingService.quoteForColeccionable(col, qty, null);
    return ResponseEntity.ok(quote);
  }
}

