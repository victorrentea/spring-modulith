package victor.training.modulith;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import victor.training.modulith.catalog.impl.Product;
import victor.training.modulith.catalog.impl.ProductRepo;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewE2ETest {
  @Autowired
  MockMvc mockMvc;
  @Autowired
  ProductRepo productRepo;

  @Test
  void experiment() throws Exception {
    Long productId = productRepo.save(new Product()).id();

    addReview(productId, 3d);
    addReview(productId, 5d);
    addReview(productId, null);

    mockMvc.perform(get("/catalog/{productId}", productId))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.stars", CoreMatchers.is(4.0)));
  }

  private void addReview(Long productId, Double stars) throws Exception {
    mockMvc.perform(post("/catalog/{productId}/reviews", productId)
            .content("""
                { "stars": %f }
                """.formatted(stars))
            .contentType(APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful());
  }

}
