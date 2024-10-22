package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryInternalApi;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GetProductApi {
  private final ProductRepo productRepo;
  private final InventoryInternalApi inventoryInternalApi;
  // example of Vertical Slice Architecture (VSA) - one class / API

  public record GetProductResponse(
      long id,
      String name,
      String description,
      int stock, // TODO display stock in product page UI
      Double price,
      Double stars
      ) {
  }

  @GetMapping("catalog/{productId}")
  public GetProductResponse execute(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
//    int stock=stockRepo.findByProductId(productId).orElseThrow().items();
    // bad because:
    // 1 coupling > impedes changes
    // 2 data semantics i might miss them, > leak domain logic from inventory to me
    int stock = inventoryInternalApi.getStock(productId);// calling through their published internal API
    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
         stock,
        product.price(),
        product.stars()
    );
  }
}
// Hints:
// 1. stock is in inventory/impl/Stock#items
// 2. ▶️ GetProductApiTest ✅
// 3. ▶️ ArchitectureTest ✅ (uses spring-modulith)
//    by default a module is only allowed to depend on classes
//    in the top-level package of another module