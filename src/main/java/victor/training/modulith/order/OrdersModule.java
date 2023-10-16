package victor.training.modulith.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.order.impl.OrderRepo;

@Service
@RequiredArgsConstructor
public class OrdersModule {
  private final OrderRepo orderRepo;

}
