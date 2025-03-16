package victor.training.modulith.catalog;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.catalog.impl.*;
import victor.training.modulith.catalog.impl.AddReviewApi.AddReviewRequest;
import victor.training.modulith.inventory.StockView;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.AFTER_METHOD;

@ApplicationModuleTest
@EntityScan(basePackageClasses = {Product.class, StockView.class /*using StockView in my @Query*/})
public class ReviewApiTest {
  @Autowired
  ProductReviewRepo reviewRepo;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  AddReviewApi addReviewApi;
  @Autowired
  GetProductApi getProductApi;

  @Transactional
  @Test
  void test() {
    Long productId = productRepo.save(new Product()).id();

    addReviewApi.call(productId, AddReviewRequest.builder().stars(4d).build());
    addReviewApi.call(productId, AddReviewRequest.builder().stars(5d).build());
    addReviewApi.call(productId, AddReviewRequest.builder().stars(null).build());

    var response = getProductApi.call(productId);
    assertThat(response.stars()).isEqualTo(4.5);
  }

  @Autowired
  DataSource dataSource;
  @Value("classpath:/data-migration.sql")
  Resource dataMigrationScript;
  @Test
  @Disabled // TODO move reviews out of Product
  @DirtiesContext(methodMode = AFTER_METHOD) // because it COMMITs data to DB
  void testWithExistingData_forMigration() throws SQLException {
    // existing data
    Product product = productRepo.save(new Product().stars(4d));
    reviewRepo.save(new ProductReview().product(product).stars(4d));

    try (Connection conn = dataSource.getConnection()) {
      ScriptUtils.executeSqlScript(conn, dataMigrationScript);
    }

    addReviewApi.call(product.id(), AddReviewRequest.builder().stars(5d).build());
    addReviewApi.call(product.id(), AddReviewRequest.builder().stars(null).build());

    var response = getProductApi.call(product.id());
    assertThat(response.stars()).isEqualTo(4.5);
  }


}
