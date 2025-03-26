package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.inventory.StockKnob;

@Slf4j
@RestController
@RequiredArgsConstructor
// example of Vertical Slice Architecture (VSA) - one class / API, no layers
public class GetProductApi {
  private final ProductRepo productRepo;
  private final InventoryInternalApi inventoryInternalApi;
  private final ReviewedProductRepo reviewedProductRepo;

  public record GetProductResponse(
      long id,
      String name,
      String description,
      int stock,
      Double price,
      Double stars
  ) {
  }

  @GetMapping("catalog/{productId}")
  public GetProductResponse call(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    int stock = inventoryInternalApi.getStock(productId)
        .map(StockKnob::stock)
        .orElse(0);
    boolean andreiFlag = false;
    double stars;
    if (andreiFlag) {
      stars = reviewedProductRepo.findByProductId(productId)
          .map(ReviewedProduct::stars)
          .orElse(0.0);
      if (stars != product.stars()) {
        log.warn("Oups, call @andrei");
        stars = product.stars();
      }
    } else {
      stars = product.stars();
    }
    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
        stock,
        product.price(),
        stars
    );
  }
}
// Hints:
// 1. stock is in inventory/impl/Stock#items
// 2. ▶️ GetProductApiTest ✅
// 3. ▶️ ArchitectureTest ✅ (uses spring-modulith)
//    by default a module is only allowed to depend on classes
//    in the top-level package of another module