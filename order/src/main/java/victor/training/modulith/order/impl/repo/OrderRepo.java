package victor.training.modulith.order.impl.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.modulith.order.impl.Order;
 // java.module Java9 Jigsaw is only meant for framework code (JPMS)
public interface OrderRepo extends JpaRepository<Order, Long> {
}
