package victor.training.modulith.inventory.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AddStockRest {
  private final StockRepo stockRepo;

  @PostMapping("stock/{productId}/add/{items}")
  @Transactional
  public void addStock(@PathVariable long productId, @PathVariable int items) {
    Stock stock = stockRepo.findByProductId(productId).orElse(new Stock().productId(productId));
    stock.add(items);
    stockRepo.save(stock);
  }
}
