package victor.training.modulith;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import victor.training.modulith.payment.PaymentGatewayWebHookApi;
import victor.training.modulith.order.impl.PlaceOrderApi;
import victor.training.modulith.shared.LineItem;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("local")
public class InitialData {
  private final PlaceOrderApi placeOrderApi;
  private final PaymentGatewayWebHookApi paymentGatewayWebHookApi;

  @EventListener(ApplicationStartedEvent.class)
  void initialData() {
    CompletableFuture.runAsync(() -> {
      try {
        var placeOrderRequest = new PlaceOrderApi.PlaceOrderRequest(
            "margareta",
            List.of(new LineItem(1L, 1)),
            "Vf. Omu");
        placeOrderApi.call(placeOrderRequest);
        log.info("Placed Initial Order");
        paymentGatewayWebHookApi.confirmPayment(1L, true);
        log.info("Paid Initial Order");
      } catch (Exception e) {
        log.error("Error while placing initial order", e);
      }
    }, CompletableFuture.delayedExecutor(500, TimeUnit.MILLISECONDS));
  }
}
