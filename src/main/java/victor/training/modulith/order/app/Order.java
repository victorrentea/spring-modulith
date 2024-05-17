package victor.training.modulith.order.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AbstractAggregateRoot;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.order.OrderStatusChangedEvent;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter(onMethod = @__(@JsonProperty))
@ToString
@Entity
@Table(name = "ORDERS")
public class Order extends AbstractAggregateRoot<Order> {
  @Id
  @GeneratedValue
  private Long id;
  private LocalDate placedOn = LocalDate.now();
  @Setter
  @NotNull
  private String customerId;
  @Setter
  private String shippingAddress;
  @Setter
  @NotNull
  private Double total;

  private OrderStatus status = OrderStatus.AWAITING_PAYMENT;
  private String shippingTrackingNumber;

  @ElementCollection
  @Setter
  @NotNull
  @NotEmpty
  private Map<Long, Integer> items = new HashMap<>();

  public Order paid(boolean ok) {
    status.requireOneOf(OrderStatus.AWAITING_PAYMENT);
    status = ok ? OrderStatus.PAYMENT_APPROVED : OrderStatus.PAYMENT_FAILED;
    // Magic: all events registered are published by Spring at repo.save(this)
    registerEvent(new OrderStatusChangedEvent(id, status, customerId));
    return this;
  }

  public Order scheduleForShipping(String trackingNumber) {
    status.requireOneOf(OrderStatus.PAYMENT_APPROVED);
    status = OrderStatus.SHIPPING_IN_PROGRESS;
    shippingTrackingNumber = trackingNumber;
    registerEvent(new OrderStatusChangedEvent(id, status, customerId));
    return this;
  }

  public void shipped(boolean ok) {
    status.requireOneOf(OrderStatus.SHIPPING_IN_PROGRESS);
    status = ok ? OrderStatus.SHIPPING_COMPLETED : OrderStatus.SHIPPING_FAILED;
    registerEvent(new OrderStatusChangedEvent(id, status, customerId));
  }

}
