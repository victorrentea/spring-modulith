package victor.training.modulith.catalog.impl;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AddReviewApi {
  private final ProductRepo productRepo;
  private final ProductReviewRepo productReviewRepo;

  @Builder
  public record AddReviewRequest(
      String title,
      String contents,
      Double stars
  ) {
  }

  @PostMapping("catalog/{productId}/reviews")
  @Transactional
  public void addReview(@PathVariable long productId, @RequestBody AddReviewRequest request) {
    Product product = productRepo.findById(productId).orElseThrow();
    ProductReview review = new ProductReview()
        .title(request.title())
        .contents(request.contents())
        .stars(request.stars())
        .createdAt(LocalDate.now());

    // CURRENT reviews & stars -> Product
    review.product(product);
    product.reviews().add(review);
    product.stars(computeAverageStars(product.reviews()));

    // NEXT: reviews & stars -> ReviewedProduct
    // 1) find or create a ReviewedProduct
    // 2) do the same as CURRENT above

    productReviewRepo.save(review);
  }

  private Double computeAverageStars(List<ProductReview> reviews) {
    return reviews
        .stream()
        .flatMap(productReview -> productReview.stars().stream())
        .mapToDouble(Double::doubleValue)
        .average()
        .stream()
        .boxed()
        .findFirst()
        .orElse(null);
  }
}
