package victor.training.modulith.inventory.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.modulith.catalog.impl.Product;
import victor.training.modulith.inventory.model.StockReservation;

import java.util.List;
import java.util.stream.IntStream;

public interface StockReservationRepo extends JpaRepository<StockReservation, Long> {
  void deleteAllByOrderId(long orderId);
  List<StockReservation> findAllByProductId(Long productId);
}
