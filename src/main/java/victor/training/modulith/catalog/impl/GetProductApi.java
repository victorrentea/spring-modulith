package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.inventory.controller.GetStockApi;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GetProductApi {
  private final ProductRepo productRepo;
  private final InventoryInternalApi inventoryInternalApi;

  public record GetProductResponse(
      long id,
      String name,
      String description,
      int stock, // TODO CR
      // Push back on the user need, and DON'T change you BE contrac. instead:
      // a) your FE after fetching the details, call inventory BE with fetch("/inventory/product/{id}/stock").
      //   + 1 network call might hurt?
      // b) Microfrontends: embed in your catalog FE page a <stock> FE compo made with❤️ by inventory team
      Double price,
      Double stars
  ) {
  }

  @GetMapping("catalog/{productId}")
  public GetProductResponse getProduct(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
//    int stock = stockRepo.findByProductId(productId).map(Stock::items).orElse(0);
// breaks the arch tests, doesn't compile if impl is a module i don't depend on

    // Option #1: tell the other team to impl it for you; until then, you're blocked! ETA: max 3 months
    // Option #2: DIY and ask them for a review
    int stock = inventoryInternalApi.getStockByProduct(product.id()).orElse(-1);
    // Option #3: open source model (everyone knows everything is the dream of business.
    // It's paradise for them: They can parachute you wherever there is budget to do stuff.
    //Can become a nightmare for developers that are landing in a minefield of complex business rules they don't have a clue about
    // Feature team (dev writing code💩 wherever necessary) vs Component Team/SME (maintaining at least review- of complex code)

    // alternative: call the REST API of the other module
//    int stock2 = new RestTemplate().getForObject("http://localhost:8080/stock/" + product.id(), Integer.class);
    // + One canonical way to get the information. Don't repeat yourself. DRY principle
    // - changes in their REST APIs will impact me
    // - changes in internal api (more often, higher volatility) will impact REST API😱😱😱
    // + if tomorrow I plan to eject 🚀 a module as a separate deployment
    // - network call (but to localhost) ⇒ CPU work to [de]JSONify, failures😱
    // - competing with actual external clients on HTTP-threads/sockets/memory
    // - heavier testing 🤔, not just a Mockito(Java) / Moq|NSubstitute(C#) => WireMock|MockServer returning canned from :9999
    // + ❤️❤️❤️ metrics
//    Integer stock1 = getStockApi.getStock(product.id()); ❌❌❌❌

    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
        stock,
        product.price(),
        product.stars()
    );
  }
  private final GetStockApi getStockApi;
}
// Tip: stock is in inventory/impl/Stock#items
// Tip: you are only allowed to use exposed classes of another modules
//     (that is, by default, the module's root package)