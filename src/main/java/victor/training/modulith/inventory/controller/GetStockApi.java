package victor.training.modulith.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.service.StockService;

@RestController
@RequiredArgsConstructor
public class GetStockApi {

  private final StockRepo stockRepo;
  private final StockService stockService;

  @GetMapping("stock/{productId}") // for other remote suystems to call
  @Transactional
//  @Secured("ROLE_ADMIN")
  public Integer execute(@PathVariable long productId) {
    return stockService.getStock(productId);
  }
}
