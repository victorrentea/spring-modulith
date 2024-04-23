package victor.training.modulith.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.service.FindStockService;

@RestController
@RequiredArgsConstructor
public class GetStockApi {
  private final FindStockService findStockService;

  @GetMapping("stock/{productId}")
  @Transactional
  public Integer execute(@PathVariable long productId) {
    return findStockService.findStock(productId);
  }

}
