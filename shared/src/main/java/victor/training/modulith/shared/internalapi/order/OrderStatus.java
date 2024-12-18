package victor.training.modulith.shared.internalapi.order;

public enum OrderStatus {
  AWAITING_PAYMENT,

  PAYMENT_FAILED,
  PAYMENT_APPROVED,

  SHIPPING_IN_PROGRESS,
  SHIPPING_FAILED,
  SHIPPING_COMPLETED
}
