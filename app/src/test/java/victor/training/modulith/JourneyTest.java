package victor.training.modulith;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@TestInstance(PER_CLASS) // stateful test !!
@TestMethodOrder(OrderAnnotation.class)
public class JourneyTest {


  @Test
  void searchCatalog() {

  }

  @Test
  void getProductDetails() {

  }

  @Test
  void addStock() {

  }

  @Test
  void getStock() {

  }
  @Test
  void placeOrder() {
      
  }

  @Test
  void callbackFromPaymentGateway() {

    // email is sent
  }

  @Test
  void getProductDetails_showsStock() {

  }
}
