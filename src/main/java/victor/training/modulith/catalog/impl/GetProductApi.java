package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryModule;
import victor.training.modulith.inventory.impl.StockRepo;

@RestController
@RequiredArgsConstructor
public class GetProductApi {
  private final ProductRepo productRepo;
  private final InventoryModule inventoryModule;

  // Think> why does my module provide in its REST API data from another module?
  //a) a BFF/proxy in front
  //b) include in your product page a dedicated <stock> webcomponent developed by the inventory full-stack team
  public record GetProductResponse(long id,
                                       String name,
                                       String description,
                                       int stock, // TODO display stock in product page UI

                            double price) {}
  @GetMapping("catalog/{productId}")
  public GetProductResponse getProduct(@PathVariable long productId) {
    Integer stock = inventoryModule.getStock(productId); // query cross-module
    Product product = productRepo.findById(productId).orElseThrow();
    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
        stock,
        product.price());
  }
  // use a queue with commands to READ DATA - NEVER DO THAT
  // to read data always use a sync call (method call, REST GET in microservices)

}
