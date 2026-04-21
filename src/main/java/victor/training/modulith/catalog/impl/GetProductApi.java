package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import victor.training.modulith.inventory.InventoryInternalApi;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GetProductApi {
  private final ProductRepo productRepo;
  private final InventoryInternalApi inventoryInternalApi;

  public record GetProductResponse(
      long id,
      String name,
      String description,
      int stock, //TODO
      Double price,
      Double stars
  ) {
  }

  @GetMapping("catalog/{productId}")
  public GetProductResponse getProduct(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    // call them via the API they expose to me
    int stock = inventoryInternalApi.getStockByProductId(product.id());

    // rest-calling stock/{productId} =
    // 🙁 slow/wasteful
    // 😊 less meeting / less DIY
    // 😊 just before ejecting one of us a microservice (separate deployment)
//    int stock = RestClient.builder().build()
//        .get()
//        .uri("http://localhost:8080/stock/{productId}", product.id())
//        .retrieve()
//        .body(Integer.class);
    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
        stock,
        product.price(),
        product.stars()
    );
  }
}



// Tip: stock is in inventory/impl/Stock#items
// Tip: you are only allowed to use exposed classes of another modules
//     (that is, by default, the module's root package)