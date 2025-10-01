package victor.training.modulith.e2e;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.catalog.impl.GetProductApi;
import victor.training.modulith.catalog.impl.Product;
import victor.training.modulith.catalog.impl.ProductRepo;
import victor.training.modulith.inventory.controller.AddStockApi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Transactional
@Disabled("TODO")
public class GetProductApiE2ETest {
  @Autowired
  ProductRepo productRepo;
  @Autowired
  AddStockApi addStockApi;
  @Autowired
  GetProductApi getProductApi;

  @Test
  void returnsStock() {
    Long productId = productRepo.save(new Product()).id();
    addStockApi.addStock(productId, 5);

    var result = getProductApi.getProduct(productId);

    assertThat(result.stock()).isEqualTo(5);
  }

}
