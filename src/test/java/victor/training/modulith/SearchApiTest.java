package victor.training.modulith;

import org.junit.jupiter.api.BeforeEach;
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
import victor.training.modulith.catalog.impl.SearchApi;
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
public class SearchApiTest {
  @Autowired
  MockMvc mockMvc;
  @Autowired
  ProductRepo productRepo;
  @Autowired
  StockRepo stockRepo;
  @Autowired
  SearchApi searchApi;

  Long outOfStockId;
  Long inStockId;

  @BeforeEach
  final void setup() throws Exception {
    inStockId = productRepo.save(new Product().name("a1")).id();
    outOfStockId = productRepo.save(new Product().name("a2")).id();
    mockMvc.perform(post("/stock/{productId}/add/{items}", inStockId, 3));
  }

  @Test
  @Disabled // TODO
  void showsOnlyItemsInStock() throws Exception {
    PageRequest pageRequest = PageRequest.of(0, 10, DESC, "name");

    var results = searchApi.call("a", pageRequest);

    assertThat(results.get(0).id()).describedAs("If this failed, the item out of stock was returned")
        .isEqualTo(inStockId);
  }

  @Test
  @Disabled // TODO
  void showsOnlyItemsInStock_paginated() throws Exception {
    PageRequest pageRequest = PageRequest.of(0, 1, DESC, "name");

    var results = searchApi.call("a", pageRequest);

    assertThat(results).describedAs("If this failed, you probably .filter()ed after query-pagination")
        .hasSize(1);
  }

}
