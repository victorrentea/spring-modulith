package victor.training.modulith.inventory.impl;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockReservationRepo extends JpaRepository<StockReservation, Long> {
  void deleteAllByOrderId(long orderId);
}
