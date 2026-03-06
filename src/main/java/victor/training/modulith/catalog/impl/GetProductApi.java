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
      int stock, // TODO
      Double price,
      Double stars
  ) {
  }

  @GetMapping("catalog/{productId}")
  public GetProductResponse getProduct(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
//    int stock = stockRepo.findByProductId(productId).map(Stock::items).orElse(0);
// breaks the arch tests, doesn't compile if impl is a module i don't depend on

    // Option #1: tell the other team to impl it for you; until then, you're blocked! ETA: max 3 months
    // Option #2: DIY and ask them for a review
    int stock = inventoryInternalApi.getStockByProduct(product.id()).orElse(-1);
    // Option #3: open source model (everyone knows everything is the dream of business.
    // It's paradise for them: They can parachute you wherever there is budget to do stuff.
    //Can become a nightmare for developers that are landing in a minefield of complex business rules they don't have a clue about
    // Feature team (dev writing code💩 wherever necessary) vs Component Team/SME (maintaining at least review- of complex code)


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