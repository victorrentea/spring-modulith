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
    //a) don't: make an Agg/BFF or FE do it!
    //b) call inventory REST API (waste) but do IT if me or them will move out soon (separate deploy)
    //c) call inventory internal API (java)
    //  c1) raise them a ticket
    //  c2) implement it yourself in their module and ask for a 4-eye review
    //  c3) use their repo and //TODO fixme @inventory
    int stock = inventoryInternalApi.getStockByProduct(productId);

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