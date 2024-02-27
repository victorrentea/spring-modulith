package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.order.impl.repo.OrderRepo;

@RestController
@RequiredArgsConstructor
public class GetOrderApi {
  private final OrderRepo orderRepo;

  @GetMapping("order/{orderId}")
  public Order getOrder(@PathVariable long orderId) {
    return orderRepo.findById(orderId).orElseThrow();
  }
}
