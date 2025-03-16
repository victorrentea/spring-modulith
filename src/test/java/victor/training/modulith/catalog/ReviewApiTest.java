package victor.training.modulith.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.catalog.impl.AddReviewApi;
import victor.training.modulith.catalog.impl.AddReviewApi.AddReviewRequest;
import victor.training.modulith.catalog.impl.GetProductApi;
import victor.training.modulith.catalog.impl.Product;
import victor.training.modulith.catalog.impl.ProductRepo;
import victor.training.modulith.inventory.StockView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ApplicationModuleTest
@Transactional
@EntityScan(basePackageClasses = {Product.class, StockView.class /*using StockView in my @Query*/})
public class ReviewApiTest {
  @Autowired
  ProductRepo productRepo;
  @Autowired
  AddReviewApi addReviewApi;
  @Autowired
  GetProductApi getProductApi;

  @Test
  void test() {
    Long productId = productRepo.save(new Product()).id();

    var review = AddReviewRequest.builder();
    addReviewApi.call(productId, review.stars(4d).build());
    addReviewApi.call(productId, review.stars(5d).build());
    addReviewApi.call(productId, review.stars(null).build());

    var response = getProductApi.call(productId);
    assertThat(response.stars()).isEqualTo(4.5);
  }


}
