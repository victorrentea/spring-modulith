package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.shared.api.inventory.InventoryInternalApi;

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
      int stock,
      Double price,
      Double stars
  ) {
  }

  @GetMapping("catalog/{productId}")
  public GetProductResponse call(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    int stock = inventoryInternalApi.findStockByProductId(productId).orElseThrow();
    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
        stock,
        product.price(),
        product.stars()
    );
  }
//  @ReviewMe("blue-team")
//  public Optional<Integer> findStockByProductId(long productId) {
//    Optional<Stock> stock = stockRepo.findByProductId(productId);
//    if (stock.isEmpty()) {
//      return Optional.empty();
//    }
//    int reservedItems = stockReservationRepo.getStockReservationsByProductId(productId)
//        .stream()
//        .mapToInt(StockReservation::items).sum();
//    return Optional.of(stock.get().items() /*- reservedItems*/);
//  }
}
// Tip: stock is in inventory/impl/Stock#items
// Tip: you are only allowed to use exposed classes of another modules
//     (that is, by default, the module's root package)