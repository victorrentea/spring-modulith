package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CreateProductApi {
  private final ProductRepo productRepo;

  public record CreateProductRequest(String name, String description, Double price) {}

  @PostMapping("catalog/product")
  public Long createProduct(@RequestBody CreateProductRequest request) {

    Product product = new Product()
        .name(request.name())
        .description(request.description())
        .price(request.price());
    productRepo.save(product);
    return product.id();
  }
}
