package victor.training.modulith.inventory.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.modulith.inventory.model.StockReservation;

public interface StockReservationRepo extends JpaRepository<StockReservation, Long> {
  void deleteAllByOrderId(long orderId);
}
