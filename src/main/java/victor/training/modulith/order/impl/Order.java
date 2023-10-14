package victor.training.modulith.order.impl;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.domain.AbstractAggregateRoot;
import victor.training.modulith.shared.ProductId;
import victor.training.modulith.order.OrderConfirmedEvent;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static victor.training.modulith.order.impl.Order.Status.AWAITING_PAYMENT;
import static victor.training.modulith.order.impl.Order.Status.CONFIRMED;

@Data
@Entity
@Table(name = "ORDERS")
public class Order extends AbstractAggregateRoot<Order> {
  public enum Status {
    AWAITING_PAYMENT, CONFIRMED;

  }
  @Id
  @GeneratedValue
  private Long id;

  private LocalDate placedOn;
  private String customerId;
  private Double total;
  private Status status = AWAITING_PAYMENT;
  private String shipmentId;
  @ElementCollection
  private Map<ProductId, Integer> items = new HashMap<>();


  public void confirm(String shipmentId) {
    status = CONFIRMED;
    this.shipmentId = shipmentId;
    registerEvent(new OrderConfirmedEvent(id));
  }

}
