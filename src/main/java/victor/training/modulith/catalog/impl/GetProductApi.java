package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryModule;
import victor.training.modulith.inventory.impl.Stock;
import victor.training.modulith.inventory.impl.StockRepo;

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
    //1: illegal access to internals
//    int stock = stockRepo.findByProductId(productId).map(Stock::items).orElse(0); //bad: breaks encapsulation
    //2: using their internal API
     int stock = inventoryModule.getAvailableStock(productId);

     // 3: DB view access - BAD here
     // idea: why can't we JOIN the STOCK_VIEW here?

    // 4: Microfrontends: Why can't the FE hit the Stock API
    // - WHY is the FE asking CATALOG for the stock (Mind-blowing idea)
    // - because you have to display it.
    // - Why can't the FE hit the Stock API directly by importing in my CATALOG screen
    // a shared component developed by the INVENTORY team?
    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
        stock,
        product.price());
  }
}
