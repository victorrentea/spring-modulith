package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.app.entity.Stock;
import victor.training.modulith.inventory.app.repo.StockRepo;
import victor.training.modulith.inventory.in.door.InventoryModule;

@RestController
@RequiredArgsConstructor
public class GetProductApi {
  private final ProductRepo productRepo;
  private final InventoryModule inventoryModule;

  public record GetProductResponse(long id,
                            String name,
                            String description,
//                            int stock, // TODO 1 CR: display stock in UI
                           // TODO task for me, (i am fullstack) to fetch stock from UI: provide a <div>
                           //  webcomponent /ui/stock called <current-stock> taht the catalog UI here is going to use
                            double price) {}

  // email from architect out of the blue: WRONG! Module-in-the-middle design smell
  @GetMapping("catalog/{productId}")
  public GetProductResponse getProduct(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
//    var stock = inventoryModule.getStock(productId);
    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
//        stock,
        product.price());
  }
}
