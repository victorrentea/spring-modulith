package victor.training.modulith.order.internalapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.order.impl.OrderService;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderModuleApi {
  private final OrderService orderService;

  public void onPaymentConfirmed(long orderId, boolean ok) {
    log.info("Payment confirmed for order {}", orderId);
    orderService.onOrderPaid(orderId, ok);
  }
}
