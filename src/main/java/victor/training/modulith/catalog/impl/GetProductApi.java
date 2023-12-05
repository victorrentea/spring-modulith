package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetProductApi {
  private final ProductRepo productRepo;

  public record GetProductResponse(long id,
                            String name,
                            String description,
//                            int stock, // TODO 1 CR: display stock in UI
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
