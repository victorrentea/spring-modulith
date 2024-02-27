package victor.training.modulith.order.impl;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

  //#1 BAD BAD BAD
//  @ManyToOne
//  private Customer customer; // BAD: coupling to domain model of another module

  // #2 internal API on the fly to customer module to get the phone
  // no network penalty besides SQL +1 QUERY hit
  // RISK: performance +1 SELECT
  // Also: see getPhoneBulk
  // IMPOSSIBLE TO JOIN WITH CUSTOMER!! ORDER BY PHONE

  // #3 replication: copy some data in: risk: stale data
  //a)
//  private String customerPhone;
  //b)
//  private CustomerStuff customerStuff; // stored as JSON in CLOB
  // c)
//  @ManyToOne // separate @Entity + table in the order
//  private OrderingCustomer customer;

  /// #4 JOIN CUSTOMER.CUSTOMER = BAD

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

  private void requireStatus(OrderStatus... allowed) {
    if (!List.of(allowed).contains(status)) {
      throw new IllegalArgumentException("Illegal state: " + status + ". Expected one of: " + Arrays.toString(allowed));
    }
  }

  public Order paid(boolean ok) {
    requireStatus(OrderStatus.AWAITING_PAYMENT);
    status = ok ? OrderStatus.PAYMENT_APPROVED : OrderStatus.PAYMENT_FAILED;
    log.info("Order status changed: {}", this);
    registerEvent(new OrderStatusChangedEvent(id, status, customerId));
    return this;
  }

  public Order scheduleForShipping(String trackingNumber) {
    requireStatus(OrderStatus.PAYMENT_APPROVED);
    status = OrderStatus.SHIPPING_IN_PROGRESS;
    shippingTrackingNumber = trackingNumber;
    log.info("Order status changed: {}", this);
    registerEvent(new OrderStatusChangedEvent(id, status, customerId));
    return this;
  }

  public void shipped(boolean ok) {
    requireStatus(OrderStatus.SHIPPING_IN_PROGRESS);
    status = ok ? OrderStatus.SHIPPING_COMPLETED : OrderStatus.SHIPPING_FAILED;
    log.info("Order status changed: {}", this);
    registerEvent(new OrderStatusChangedEvent(id, status, customerId));
  }

}
