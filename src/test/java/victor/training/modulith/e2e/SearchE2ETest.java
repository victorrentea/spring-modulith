package victor.training.modulith.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.catalog.impl.CreateProductApi;
import victor.training.modulith.catalog.impl.CreateProductApi.CreateProductRequest;
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
public class SearchE2ETest {
  @Autowired
  CreateProductApi createProductApi;
  @Autowired
  SearchApi searchApi;
  @Autowired
  AddStockApi addStockApi;

  @Test
  void returnsProductsMatchingName() {
    long productId = createProductApi.createProduct(new CreateProductRequest("xa1","",0d));
    addStockApi.call(productId, 3);
    createProductApi.createProduct(new CreateProductRequest("b","",0d));
    addStockApi.call(productId, 3);

    var results = searchApi.search("a", null);

    assertThat(results)
        .hasSize(1)
        .first()
        .returns(productId, ProductSearchResult::id);
  }
  @Test
  void doesNotReturnProductsOutOfStock() {
    createProductApi.createProduct(new CreateProductRequest("b","",0d));

    var results = searchApi.search("b", null);

    assertThat(results).isEmpty();
  }

  @Test
  void paginationWorks() {
    long productId = createProductApi.createProduct(new CreateProductRequest("a1","",0d));
    addStockApi.call(productId, 3);
    var product2Id = createProductApi.createProduct(new CreateProductRequest("a2","",0d));
    addStockApi.call(product2Id, 3);

    PageRequest pageRequest = PageRequest.of(0, 1, ASC, "name");
    var results = searchApi.search("a", pageRequest);

    assertThat(results)
        .describedAs("If this failed, you probably .filter()ed after query-pagination")
        .map(ProductSearchResult::id)
        .containsExactly(productId);
  }

}
