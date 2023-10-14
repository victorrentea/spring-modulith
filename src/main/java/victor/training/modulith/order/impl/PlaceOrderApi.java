package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.order.Catalog;
import victor.training.modulith.shared.ProductId;
import victor.training.modulith.order.infra.PaymentService;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlaceOrderApi {
  private final OrderRepo orderRepo;
  private final Catalog catalogDoor;
  private final PaymentService paymentService;

  record PlaceOrderRequest(String customerId, Map<ProductId, Integer> items) {
  }

  @PostMapping("order")
  public String placeOrder(@RequestBody PlaceOrderRequest request) {
    Map<ProductId, Double> prices = catalogDoor.getManyPrices(request.items().keySet());
    double total = request.items.entrySet().stream()
        .mapToDouble(e -> e.getValue() * prices.get(e.getKey()))
        .sum(); // TODO +OrderLine?
    Order order = new Order()
        .placedOn(LocalDate.now())
        .items(request.items)
        .customerId(request.customerId)
        .total(total);
    orderRepo.save(order);
    return paymentService.generatePaymentUrl(order);
  }
}
