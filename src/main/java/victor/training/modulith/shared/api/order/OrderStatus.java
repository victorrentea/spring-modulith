package victor.training.modulith.shared.api.order;

public enum OrderStatus {
  AWAITING_PAYMENT,

  PAYMENT_FAILED,
  PAYMENT_APPROVED,

  SHIPPING_IN_PROGRESS,
  SHIPPING_FAILED,
  SHIPPING_COMPLETED
}
