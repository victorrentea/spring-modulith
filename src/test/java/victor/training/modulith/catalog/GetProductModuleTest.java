package victor.training.modulith.catalog;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.catalog.impl.GetProductApi;
import victor.training.modulith.catalog.impl.Product;
import victor.training.modulith.catalog.impl.ProductRepo;
import victor.training.modulith.inventory.StockView;
import victor.training.modulith.inventory.api.AddStockApi;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationModuleTest
@EntityScan(basePackageClasses = {Product.class, StockView.class /*using StockView in my @Query*/})
@Transactional
//@Disabled
public class GetProductModuleTest {
  @Autowired
  ProductRepo productRepo;
  @Autowired
  GetProductApi getProductApi;

  @Test
  void returnsStock() {
    var product = new Product()
        .name("name")
        .description("desc")
        .price(1d);
    Long productId = productRepo.save(product).id();

    var result = getProductApi.call(productId);

    assertThat(result.name()).isEqualTo("name");
    assertThat(result.description()).isEqualTo("desc");
    assertThat(result.price()).isEqualTo(1d);
  }
}
