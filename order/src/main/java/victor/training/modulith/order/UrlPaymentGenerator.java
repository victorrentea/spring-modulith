package victor.training.modulith.order;

// what if there are multiple implementations
// of the same interface in different modules?

// PayU/Stripe/GPay/ApplePay/... are 'plugins'
public interface UrlPaymentGenerator {
}
