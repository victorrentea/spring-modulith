package victor.training.modulith.inventory.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.modulith.inventory.model.StockReservation;

import java.util.List;

public interface StockReservationRepo extends JpaRepository<StockReservation, Long> {
  void deleteAllByOrderId(long orderId);
  List<StockReservation> findAllByProductId(Long productId);
}
