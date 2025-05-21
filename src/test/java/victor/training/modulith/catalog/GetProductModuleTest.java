package victor.training.modulith.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.catalog.impl.GetProductApi;
import victor.training.modulith.catalog.impl.Product;
import victor.training.modulith.catalog.impl.ProductRepo;
import victor.training.modulith.shared.api.inventory.InventoryInternalApi;
import victor.training.modulith.shared.api.inventory.StockView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ApplicationModuleTest // spring modulith starts only your slice of the spring app
@EntityScan(basePackageClasses = {Product.class, StockView.class /*using StockView in my @Query*/})
@Transactional
public class GetProductModuleTest {
  @Autowired
  ProductRepo productRepo;
  @Autowired
  GetProductApi getProductApi;
  @MockBean
  InventoryInternalApi inventoryInternalApi;

  @Test
  void returnsStock() {
    var product = new Product()
        .name("name")
        .description("desc")
        .price(1d);
    Long productId = productRepo.save(product).id();
    when(inventoryInternalApi.getStockByProduct(productId)).thenReturn(10);

    var result = getProductApi.call(productId);

    assertThat(result.name()).isEqualTo("name");
    assertThat(result.description()).isEqualTo("desc");
    assertThat(result.price()).isEqualTo(1d);
    assertThat(result.stock()).isEqualTo(10);
  }
}
