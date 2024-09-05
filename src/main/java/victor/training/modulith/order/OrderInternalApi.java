package victor.training.modulith.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.order.impl.OrderService;

@Service
@RequiredArgsConstructor
public class OrderInternalApi {
  private final OrderService orderService;

//  public void confirmPayment(long orderId, boolean ok) {
//    orderService.confirmPayment(orderId, ok);
//  }
}
