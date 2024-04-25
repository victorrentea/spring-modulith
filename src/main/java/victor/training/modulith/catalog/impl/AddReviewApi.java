package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AddReviewApi {
  private final ProductRepo productRepo;
  private final ProductReviewRepo productReviewRepo;

  public record AddReviewRequest(
      String title,
      String contents,
      Double stars
  ) {
  }

  @PostMapping("catalog/{productId}/reviews")
  @Transactional
  public void execute(@PathVariable long productId, @RequestBody AddReviewRequest request) {
    Product product = productRepo.findById(productId).orElseThrow();
    ProductReview review = new ProductReview()
        .title(request.title())
        .contents(request.contents())
        .stars(request.stars())
        .createdAt(LocalDate.now());
    product.reviews().add(review);
    review.product(product);

    Double newStars = product.reviews()
        .stream()
        .flatMap(productReview -> productReview.stars().stream())
        .mapToDouble(Double::doubleValue)
        .average()
        .stream()
        .boxed()
        .findFirst()
        .orElse(null);
    product.stars(newStars);
    productReviewRepo.save(review);
  }
}
