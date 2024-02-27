package victor.training.modulith.order;

// isn't this test-induced design damage?
public interface PaymentDoor {
  String generatePaymentUrl(long orderId, double total);
}

//1) in general dendency inversion we do to DEFEND against a filthy adapter.
// The contract (interface) is CLEAN while the implementation is "dirty"

// 2) + this Dep Inversion has to translation to a microservice ecosystem
// this is just a purely OOP hack to revert some code dependency