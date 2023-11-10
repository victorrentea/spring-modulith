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

  public record GetProductResponse(long id,
                            String name,
                            String description,
//                            int stock, // TODO 1
                            double price) {}

  @GetMapping("catalog/{productId}")
  public GetProductResponse getProduct(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
//        stock,
        product.price());
  }
}
