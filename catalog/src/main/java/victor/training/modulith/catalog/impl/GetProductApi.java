package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryModuleInterface;

@RestController
@RequiredArgsConstructor
public class GetProductApi {
  private final ProductRepo productRepo;
//  private final StockRepo stockRepo;// DAO/EJB = coupling
  // BAD
  // 1) REST Resource -> Repo: skipping a layer layers= bad
  // 2) Accessing internal data model/repo/EJB of another module
  // GOOD
  // 1) call the REST API of the other module over http://localhost
  // - WASTEFUL: involves network socket, de/serialization, CPU
  // 2) call a method of the inventory public API
  // that api can be
  // a) in inventory that I imported via pom.xml => allows me to see/accidentally depend on stuff I'm not supporsed
  //    - BAD can generate module build cycles
  //    - BAD you can SEE in THEIR impl classes = polluting ctrl-space (DevEx🤮)
  // b) 'interfaces' module (of all the modules)
  //    - accidental coupling between APIs
  //    - harder to oversee module deps
  //    - risk: impl module not in classpath => failure at startup of a partial release
  // c) an 'inventory-api' module
  //    - BAD: doubles the number of modules  (DevEx🤮)


  private final InventoryModuleInterface inventoryModule;

  public record GetProductResponse(long id,
                                   String name,
                                   String description,
                                   int stock, // TODO display stock in product page UI
                                   double price) {}

  @GetMapping("catalog/{productId}")
  public GetProductResponse getProduct(@PathVariable long productId) {
    Product product = productRepo.findById(productId).orElseThrow();
    // call GetStockApi using httpClient over REST
    int stock = inventoryModule.getStockByProduct(productId);
    return new GetProductResponse(product.id(),
        product.name(),
        product.description(),
        stock,
        product.price());
  }
  // WHY do I (the catalog) have to return the data coming from INVENTORY?
  // a) include in your UI a webcomponent developed by Inventory team
  // b) create a "site" module that acts like a BFF/ApiGateway aggeregating data from different modules
  // just like a Facade, to allow the modules NOT to depend on each otehr
}
