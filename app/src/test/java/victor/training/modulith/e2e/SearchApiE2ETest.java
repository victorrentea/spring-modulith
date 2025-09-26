package victor.training.modulith.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import victor.training.modulith.catalog.impl.CreateProductApi;
import victor.training.modulith.catalog.impl.CreateProductApi.CreateProductRequest;
import victor.training.modulith.catalog.impl.ProductRepo;
import victor.training.modulith.catalog.impl.SearchApi;
import victor.training.modulith.catalog.impl.SearchApi.ProductSearchResult;
import victor.training.modulith.inventory.api.AddStockApi;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.service.StockService;
import victor.training.modulith.shared.LineItem;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.ASC;

@SpringBootTest
public class SearchApiE2ETest {
  public static final SearchApi.ProductSearchCriteria CRITERIA = new SearchApi.ProductSearchCriteria("a", "");
  @Autowired
  CreateProductApi createProductApi;
  @Autowired
  SearchApi searchApi;
  @Autowired
  AddStockApi addStockApi;
  @Autowired
  private StockRepo stockRepo;
  @Autowired
  private ProductRepo productRepo;
  @Autowired
  private StockService stockService;

  @BeforeEach
  final void before() {
    stockRepo.deleteAll();
    productRepo.deleteAll();
  }

  @Test
  void returnsProductsMatchingName() {
    long matchingProductId = createProductApi.createProduct(new CreateProductRequest("xa1", "", 0d));
    addStockApi.call(matchingProductId, 3);
    long notMatchingProductId = createProductApi.createProduct(new CreateProductRequest("b", "", 0d));
    addStockApi.call(notMatchingProductId, 3);

    var results = searchApi.search(CRITERIA, null);

    assertThat(results)
        .hasSize(1)
        .first()
        .returns(matchingProductId, ProductSearchResult::id);
  }

  @Test
  void paginationWorks() {
    long matchId = createProductApi.createProduct(new CreateProductRequest("a1", "", 0d));
    addStockApi.call(matchId, 3);
    var noMatchId = createProductApi.createProduct(new CreateProductRequest("a2", "", 0d));
    addStockApi.call(noMatchId, 3);

    PageRequest pageRequest = PageRequest.of(0, 1, ASC, "name");
    var results = searchApi.search(CRITERIA, pageRequest);

    assertThat(results)
        .describedAs("If this failed, you probably .filter()ed after query-pagination")
        .map(ProductSearchResult::id)
        .containsExactly(matchId);
  }

  @Test
//  @Disabled("TODO")
  void doesNotReturnProductsOutOfStock() {
    createProductApi.createProduct(new CreateProductRequest("a", "", 0d));

    var results = searchApi.search(CRITERIA, null);

    assertThat(results).isEmpty();
  }

  //  @Disabled("TODO")
  @Nested
  class DoesNotReturnProductsOutOfStock {
    @Test
    void stockNull() throws InterruptedException {
      createProductApi.createProduct(new CreateProductRequest("a", "", 0d));

      Thread.sleep(100);
      var results = searchApi.search(CRITERIA, null);

      assertThat(results).isEmpty();
    }

    @Test
    void stock1() throws InterruptedException {
      Long productId = createProductApi.createProduct(new CreateProductRequest("a", "", 0d));
      addStockApi.call(productId, 1);

      Thread.sleep(100);
      var results = searchApi.search(CRITERIA, null);

      assertThat(results).hasSize(1);
    }

    @Test
    void stock0() throws InterruptedException {
      Long productId = createProductApi.createProduct(new CreateProductRequest("a", "", 0d));
      addStockApi.call(productId, 1);
      stockService.reserveStock(1L, List.of(new LineItem(productId, 1)));

      Thread.sleep(100);
      var results = searchApi.search(CRITERIA, null);

      assertThat(results).isEmpty();
    }

  }

}
