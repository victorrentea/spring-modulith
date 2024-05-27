package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;

@RestController
@RequiredArgsConstructor
public class GetProductApi {
  private final ProductRepo productRepo;
  // example of Vertical Slice Architecture (VSA) - one class / API

  public record GetProductResponse(
      long id,
      String name,
      String description,
       int stock, // TODO display stock in product page UI
      Double price,
      Double stars
  ) {
  }

  private final StockRepo stockRepo;

  @GetMapping("catalog/{productId}")
  public GetProductResponse execute(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    Stock entity = stockRepo.findByProductId(productId).orElseThrow();
    int stock = entity.items();
    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
         stock,
        product.price(),
        product.stars()
    );
  }
}
