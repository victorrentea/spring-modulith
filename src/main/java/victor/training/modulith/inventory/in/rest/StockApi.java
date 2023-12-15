package victor.training.modulith.inventory.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.app.service.StockService;

@RestController
@RequiredArgsConstructor
public class StockApi {
  private final StockService service;

  @PostMapping("stock/{productId}/add/{items}")
  public void addStock(@PathVariable long productId, @PathVariable int items) {
    service.addStock(productId, items);
  }

  @GetMapping("stock/{productId}")
  public Integer addStock(@PathVariable long productId) {
    return service.addStock(productId);
  }
}
