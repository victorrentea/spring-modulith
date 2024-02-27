package victor.training.modulith.order;

// isn't this test-induced design damage?
public interface PaymentDoor {
  String generatePaymentUrl(long orderId, double total);
}

// in general dendency inversion we do to DEFEND against a filthy adapter.
// The contract (interface) is CLEAN while the implementation is "dirty"
