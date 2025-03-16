package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetOrderApi {
  private final OrderRepo orderRepo;

  @GetMapping("order/{orderId}")
  public Order call(@PathVariable long orderId) {
    return orderRepo.findById(orderId).orElseThrow();
  }
}
