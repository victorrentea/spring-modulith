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
    int stock = inventoryInternalApi.getStockForProduct(productId).orElse(Integer.MAX_VALUE);
    // a) ask them ❤️ 'they know best about their stuff' ask them to do it > raise them a ticket and wait..........1M.......6Months.......
    // b) DIY
    // - b1) THE OWNER TEAM must review the PR ("the elders"=SME) "component team"
    // - b2) I would take ownership if inventory is shared ownership component "feature-team"
    // c) SQL THEIR DB and place a // FIXME @blueteam - this is the best I could do, I selected your tables. I'm sorry. But boss said to ship sh*t please contact my colleagues for moral support, as I go in a sabbatical year.
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