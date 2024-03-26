package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.impl.Stock;
import victor.training.modulith.inventory.impl.StockRepo;

@RestController
@RequiredArgsConstructor
public class GetProductApi {
  private final ProductRepo productRepo;
  private final StockRepo stockRepo;

  public record GetProductResponse(long id,
                            String name,
                            String description,
                            int stock, // TODO display stock in product page UI
                            double price) {}

  @GetMapping("catalog/{productId}")
  public GetProductResponse getProduct(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    int stock = stockRepo.findByProductId(productId).map(Stock::items).orElse(0);
    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
        stock,
        product.price());
  }
}
