package victor.training.modulith.payment.impl;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class PaymentEvidence {
  @Id
  @GeneratedValue
  Long id;

}
