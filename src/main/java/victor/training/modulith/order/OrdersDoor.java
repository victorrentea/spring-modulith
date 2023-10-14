package victor.training.modulith.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.order.impl.OrderRepo;

@Service
@RequiredArgsConstructor
public class OrdersDoor  {
  private final OrderRepo orderRepo;

  public String getCustomerOfOrder(long orderId) {
    return orderRepo.findById(orderId).orElseThrow().customerId();
  }
}
