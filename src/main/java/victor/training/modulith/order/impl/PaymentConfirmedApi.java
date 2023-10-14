package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.customer.CustomerModule;
import victor.training.modulith.order.infra.ShipmentService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentConfirmedApi {
  private final OrderRepo orderRepo;
  private final ShipmentService shipmentService;
  private final CustomerModule customerModule;

  @PutMapping("order/{orderId}/confirm-payment")
  @Transactional
  public void confirmPayment(@PathVariable long orderId) {
    Order order = orderRepo.findById(orderId).orElseThrow();
    String address = customerModule.getCustomerAddress(order.customerId());
    String shipmentId = shipmentService.requestShipment(address);
    String email = customerModule.getCustomerEmail(order.customerId());
    log.info("Sending 'Order Confirmed' email to " + email);
    order.confirm(shipmentId);
    orderRepo.save(order);
  }

}
