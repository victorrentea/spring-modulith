package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GetProductApi {
  private final ProductRepo productRepo;
  // example of Vertical Slice Architecture (VSA) - one class / API

  public record GetProductResponse(
      long id,
      String name,
      String description,
//       int stock, // TODO display stock in product page UI
      Double price,
      Double stars
      ) {
  }

  @GetMapping("catalog/{productId}")
  public GetProductResponse execute(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    //int stock= TODO
    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
//         stock,
        product.price(),
        product.stars()
    );
  }
}
// Hints:
// 1. stock is in inventory/impl/Stock#items
// 2. ▶️ GetProductApiTest ✅
// 3. ▶️ ArchitectureTest ✅ (uses spring-modulith)
//    by default a module is only allowed to depend on classes
//    in the top-level package of another module