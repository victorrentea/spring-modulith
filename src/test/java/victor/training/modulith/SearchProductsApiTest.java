package victor.training.modulith;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.catalog.impl.Product;
import victor.training.modulith.catalog.impl.ProductRepo;
import victor.training.modulith.catalog.impl.SearchProductApi;
import victor.training.modulith.inventory.repo.StockRepo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SearchProductsApiTest {
  @Autowired
  MockMvc mockMvc;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  StockRepo stockRepo;
  @Autowired
  SearchProductApi searchProductApi;


  @Test
  @Disabled // TODO fix
  void showsOnlyItemsInStock() throws Exception {
    Long inStockId = productRepo.save(new Product().name("a1")).id();
    Long outOfStockId = productRepo.save(new Product().name("a2")).id();
    mockMvc.perform(post("/stock/{productId}/add/{items}", inStockId, 3));
    PageRequest pageRequest = PageRequest.of(0, 1, DESC, "name");

    var results = searchProductApi.execute("a", pageRequest);

    assertThat(results).describedAs("If this failed, you probably .filter()ed after query-pagination")
        .hasSize(1);
    assertThat(results.get(0).id()).describedAs("If this failed, the item out of stock was returned")
        .isEqualTo(inStockId);
  }

}
