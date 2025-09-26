package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GetProductApi {
  private final ProductRepo productRepo;
  private final InventoryInternalApi inventoryInternalApi;

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
    // Bad because: introduces coupling to internals of the other module, breaking their encapsulation, preventing them from chaning stuff tomorrow
    // FIXME @inventory-team - works for very busy teams
//    int stock = stockRepo.findByProductId(product.id()).orElseThrow().items();

    // Option: "orchestrate from above": introduce a layer 'above you' that aggregates data from you and inventory.
    // Option: if your client is FE (Browser) => tell them to call 2 apis instead of 1 fetch(1).then(r=>fetch(2))

    int stock = inventoryInternalApi.getStockByProduct(product.id());

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