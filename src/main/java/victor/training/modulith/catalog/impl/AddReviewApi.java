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
  private final ReviewedProductRepo reviewedProductRepo;

  @Builder
  public record AddReviewRequest(
      String title,
      String contents,
      Double stars
  ) {
  }

  @PostMapping("catalog/{productId}/reviews")
  @Transactional
  public void call(@PathVariable long productId, @RequestBody AddReviewRequest request) {
    Product product = productRepo.findById(productId).orElseThrow();
    ProductReview review = new ProductReview()
        .title(request.title())
        .contents(request.contents())
        .stars(request.stars())
        .createdAt(LocalDate.now());
    review.product(product);
    product.reviews().add(review);

    product.stars(computeAverageStars(product.reviews()));

    if (true) {
      ReviewedProduct reviewedProduct = reviewedProductRepo.findByProductId(productId)
          .orElseGet(() -> reviewedProductRepo.save(new ReviewedProduct().productId(productId)));
      review.reviewedProduct(reviewedProduct);
      reviewedProduct.reviews().add(review);
      reviewedProduct.stars(computeAverageStars(reviewedProduct.reviews()));
    }
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
