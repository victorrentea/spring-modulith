package victor.training.modulith.inventory.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import victor.training.modulith.inventory.app.entity.Stock;
import victor.training.modulith.inventory.app.repo.StockRepo;

@Service
@RequiredArgsConstructor
public class StockService {
  private final StockRepo stockRepo;
  @Transactional
  public void addStock(@PathVariable long productId, @PathVariable int items) {
    Stock stock = stockRepo.findByProductId(productId).orElse(new Stock().productId(productId));
    stock.add(items);
    stockRepo.save(stock);
  }

  public Integer addStock(@PathVariable long productId) {
    return stockRepo.findByProductId(productId).map(Stock::items).orElse(0);
  }
}
