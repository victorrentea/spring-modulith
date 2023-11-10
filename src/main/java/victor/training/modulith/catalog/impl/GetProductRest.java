package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetProductRest {
  private final ProductRepo productRepo;
//  private final StockDoor stockDoor;

  public record GetProductResponse(long id,
                            String name,
                            String description,
//                            int stock, // 🤔 FE can fetch it directly from Stock REST API?
                            double price) {}

  @GetMapping("catalog/{productId}")
  public GetProductResponse getProduct(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
//    int stock = stockDoor.getStock(productId);
    return new GetProductResponse(product.id(), product.name(), product.description(), /*stock,*/ product.price());
  }
}
