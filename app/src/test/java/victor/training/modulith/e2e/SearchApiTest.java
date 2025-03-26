package victor.training.modulith.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.catalog.impl.Product;
import victor.training.modulith.catalog.impl.ProductRepo;
import victor.training.modulith.catalog.impl.SearchApi;
import victor.training.modulith.catalog.impl.SearchApi.ProductSearchResult;
import victor.training.modulith.inventory.api.AddStockApi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Transactional
//@Disabled // TODO
public class SearchApiTest {
  @Autowired
  ProductRepo productRepo;
  @Autowired
  SearchApi searchApi;
  @Autowired
  AddStockApi addStockApi;

  Long productId;

  @BeforeEach
  final void setup() {
    productId = productRepo.save(new Product().name("xa1")).id();
    addStockApi.call(productId, 3);
  }

  @Test
  void returnsProductsMatchingName() {
    productRepo.save(new Product().name("b"));
    var results = searchApi.call("a", null);

    assertThat(results)
        .map(ProductSearchResult::id)
        .containsExactly(productId);
  }
  @Test
  void doesNotReturnProductsOutOfStock() {
    Long productIdOutOfStock = productRepo.save(new Product().name("b")).id();

    var results = searchApi.call("a", null);

    assertThat(results)
        .map(ProductSearchResult::id)
        .doesNotContain(productIdOutOfStock);
  }

  @Test
  void paginationWorks() {
    var product2Id = productRepo.save(new Product().name("b")).id();
    addStockApi.call(product2Id, 3);

    PageRequest pageRequest = PageRequest.of(0, 1, ASC, "name");
    var results = searchApi.call("a", pageRequest);

    assertThat(results)
        .describedAs("If this failed, you probably .filter()ed after query-pagination")
        .map(ProductSearchResult::id)
        .containsExactly(productId);
  }

}
