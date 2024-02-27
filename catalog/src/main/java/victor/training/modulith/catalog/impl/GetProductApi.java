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

  public record GetProductResponse(long id,
                            String name,
                            String description,
                            int stock, // TODO display stock in product page UI
                            double price) {}

  @GetMapping("catalog/{productId}")
  public GetProductResponse getProduct(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    int stock = inventoryModule.getStock(productId);
    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
        stock,
        product.price());
  }
}
// 1) catalog calls inventory and returns that data to its client.
// WHEN: to limit the network calls between client and me

// 3) to decouple catalog from inventory, we can introduce an additional intermediate module
// 'orchestrator'/'saga'/'bff'/'api' which to aggregate data from the 2-3-N modules

// 2) ask the client (UI) to fetch the stock with another call to inventoryRestApi
// perfectly fine when network traffic is not an issue, and you HAVE a word about your client
// => JS/TS doing two fetch() calls and all(fetch(), fetch()).then(allResponses => { ... })
// Or if you are doing micro-frontends, you can include the <div> developed by inventory that fetches the stock
// => <inventory-stock productId="123"></inventory-stock>
//Benefit any changes in UI is authored by the inventory "Running out"

