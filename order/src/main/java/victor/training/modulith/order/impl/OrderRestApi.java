package victor.training.modulith.order.impl;

import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.modulith.shared.LineItem;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderRestApi {
  private final OrderService orderService;

  @GetMapping("order/{orderId}")
  public Order get(@PathVariable long orderId) {
    return orderService.getOrder(orderId);
  }

  public record PlaceOrderRequest(
      @NotEmpty String customerId,
      @NotEmpty List<LineItem> items,
      @NotEmpty String shippingAddress) {
  }

  @PostMapping("order")
  public String placeOrder(@RequestBody @Validated PlaceOrderRequest request) {
    return orderService.doPlaceOrder(request);
  }

}
