package victor.training.modulith.order;

// interfata pentru rezolvarea ciclului de dependinte e cam fortata
//  insa rupe daca as fi avut 2+ module care implementau PaymentModuleInterface
// eg: Paypal, Stripe, PayU = Plugin-in-uri

// sau cand modulul se numeste:
// clientul-rosu si contine PaymentModulePtClientuRosu
// clientul-rosu si contine PaymentModulePtClientuAlbastru
// = client-specific customizations
public interface PaymentModuleInterface {
  String generatePaymentUrl(long orderId, double total);
}
