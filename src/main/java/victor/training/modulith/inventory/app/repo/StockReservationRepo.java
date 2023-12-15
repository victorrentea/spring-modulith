package victor.training.modulith.inventory.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.modulith.inventory.app.entity.StockReservation;

public interface StockReservationRepo extends JpaRepository<StockReservation, Long> {
  void deleteAllByOrderId(long orderId);
}
