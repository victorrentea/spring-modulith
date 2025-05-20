package victor.training.modulith.inventory.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.modulith.inventory.model.StockReservation;

import java.util.List;
import java.util.Optional;

public interface StockReservationRepo extends JpaRepository<StockReservation, Long> {
  void deleteAllByOrderId(long orderId);
  List<StockReservation> findByProductId(Long productId);
}
