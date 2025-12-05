package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.inventory.repo.StockRepo;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GetProductApi { // VSA
  private final ProductRepo productRepo;
  private final InventoryInternalApi inventoryInternalApi;

  public record GetProductResponse(
      long id,
      String name,
      String description,
      int stock, //⭐️
      Double price,
      Double stars
  ) {
  }

  @GetMapping("catalog/{productId}")
  public GetProductResponse getProduct(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
//    int stock = stockRepo.findByProductId(product.id()).orElseThrow().items(); // ❌breaks encaps
    int stock = inventoryInternalApi.getStock(product.id());
    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
        stock,
        product.price(),
        product.stars()
    );
  }
}
// Tip: stock is in inventory/impl/Stock#items
// Tip: you are only allowed to use exposed classes of another modules
//     (that is, by default, the module's root package)