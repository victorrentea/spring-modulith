package victor.training.modulith.payment.impl;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentEvidenceRepo extends JpaRepository<PaymentEvidence, Long> {
}
