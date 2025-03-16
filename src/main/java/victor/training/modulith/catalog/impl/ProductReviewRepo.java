package victor.training.modulith.catalog.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductReviewRepo extends JpaRepository<ProductReview, Long> {

}
