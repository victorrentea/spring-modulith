package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryModule;

@RestController
@RequiredArgsConstructor
public class GetProductApi {
  private final ProductRepo productRepo;
  private final InventoryModule inventoryModule;

  public record GetProductResponse(long id,
                            String name,
                            String description,
                            int stock, // TODO display stock in product page UI
                            double price) {}

  @GetMapping("catalog/{productId}")
  public GetProductResponse getProduct(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    int stock = inventoryModule.getStockByProduct(productId);
    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
        stock,
        product.price());
  }
  // WHY do I (the catalog) have to return the data coming from INVENTORY?
  // a) include in your UI a webcomponent developed by Inventory team
  // b) create a "site" module that acts like a BFF/ApiGateway aggeregating data from different modules
  // just like a Facade, to allow the modules NOT to depend on each otehr
}
