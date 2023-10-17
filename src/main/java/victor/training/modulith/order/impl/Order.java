package victor.training.modulith.order.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.AbstractAggregateRoot;
import victor.training.modulith.customer.impl.Customer;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.order.OrderStatusChangedEvent;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//  @ManyToOne
//  private Customer customer;
  private String customerId; // risk = sa dispara customerul cu acel id pt ca nu mai am FK
  // a) daca tintesti sa ramai pe Modulith ani de zile (daca asa vrei sa deployezi monolit) pastreaza FKul !!!!
  // b) daca vrei sa mergi la microservicii, tre sa dispara FK intre module intai
  //  ce ma fac?!😱 Regula: odata ce ai publicat un ID (eg Customer), nu mai ai voie sa stergi acel ID vreodata!
  //  faci deleted=true pe un camp

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

  public void paid(boolean ok) {
    requireStatus(OrderStatus.AWAITING_PAYMENT);
    status = ok ? OrderStatus.PAYMENT_APPROVED : OrderStatus.PAYMENT_FAILED;
    registerEvent(new OrderStatusChangedEvent(id, status, customerId));
    // la repo.save(acestei entitati), spring va publica evenimentul adaugate mai su
  }

  public void scheduleForShipping(String trackingNumber) {
    requireStatus(OrderStatus.PAYMENT_APPROVED);
    status = OrderStatus.SHIPPING_IN_PROGRESS;
    shippingTrackingNumber = trackingNumber;
    registerEvent(new OrderStatusChangedEvent(id, status, customerId));
  }

  public void shipped(boolean ok) {
    requireStatus(OrderStatus.SHIPPING_IN_PROGRESS);
    status = ok ? OrderStatus.SHIPPING_COMPLETED : OrderStatus.SHIPPING_FAILED;
    registerEvent(new OrderStatusChangedEvent(id, status, customerId));
  }

}
