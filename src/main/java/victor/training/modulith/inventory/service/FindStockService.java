package victor.training.modulith.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;

@Service
@RequiredArgsConstructor
public class FindStockService {
  private final StockRepo stockRepo;

  public int findStock(long productId) {
    return stockRepo.findByProductId(productId)
        .map(Stock::items)
        .orElse(0);
  }
}
