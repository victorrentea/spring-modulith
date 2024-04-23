package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryModuleApi;
import victor.training.modulith.inventory.repo.StockRepo;

@RestController
@RequiredArgsConstructor
public class GetProductApi {
  private final ProductRepo productRepo;
  private final InventoryModuleApi inventoryModuleApi;

  public record GetProductResponse(long id,
                            String name,
                            String description,
                            int stock, // TODO display stock in product page UI
                            double price) {}

  @GetMapping("catalog/{productId}")
  public GetProductResponse execute(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    int stock = inventoryModuleApi.findStock(productId);
    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
        stock,
        product.price());
  }

  // micro-frontends
  //also: you could say: why do I have to return data of another module?!!
  // how about an <inventory:stock producId="{productId}"/>
  // custom tag made with 💖 by inventory team
  // included in my product page?
}
