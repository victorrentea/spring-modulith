package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryModule;

@RestController
@RequiredArgsConstructor
public class GetProductRest {
  private final ProductRepo productRepo;
private final InventoryModule inventoryModule;

  public record GetProductResponse(long id,
                            String name,
                            String description,
//                            int stock, // 🤔 FE can fetch it directly from Stock REST API?
                            double price) {}

  @GetMapping("catalog/{productId}")
  public GetProductResponse getProduct(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
//    var stock = inventoryModule.getStock(productId); // +1 SELECT = 2ms
    // idee: replic valoarea stocului
    // idee: microfrontend: stock sa fie randat cu un <div> de-al inventory care bate direct in API lor REST
    // adica scoate campul stock de mai sus

    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
//        stock,
        product.price());
  }
}
