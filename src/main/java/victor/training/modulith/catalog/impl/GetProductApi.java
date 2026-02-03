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
  public GetProductResponse getProduct(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    // TODO display stock in the product details page in UI
    // tests fail, tomorrow Maven modules won't compile
//    int stock = stockRepo.findByProductId(product.id()).orElseThrow().items();
    int stock = inventoryInternalApi.getStockByProduct(product.id());
    // Component Team: a team / software piece
    // ⊖ Team Friction/interruption/delays, bench
    // Option#1: raise them a ticket🤞

    // Feature Team: a team / change set ± SME/"maintainers"
    // ⊖ Devs tend to write 💩 in complex areas
    // Option#2: submit a PR to the maintainer

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