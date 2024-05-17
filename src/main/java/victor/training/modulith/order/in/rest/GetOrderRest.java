package victor.training.modulith.order.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.order.app.Order;
import victor.training.modulith.order.app.OrderRepo;

@RestController
@RequiredArgsConstructor
public class GetOrderRest {
  private final OrderRepo orderRepo;

  @GetMapping("order/{orderId}")
  public Order execute(@PathVariable long orderId) {
    return orderRepo.findById(orderId).orElseThrow();
  }
}
