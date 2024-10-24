package victor.training.modulith.catalog.impl;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Data // sorry
@Table(schema = "catalog")
@SequenceGenerator(name = "product_seq", schema = "catalog")
public class Product {
  @Id
  @GeneratedValue(generator = "product_seq")
  private Long id;

  private String name;

  private String description;

  private Double price;

  private Double stars;

  private Boolean inStock;
  // Stock-service has to keep up to date catalog-service/Product.inStock
// event design: what name and contents?
// -ProductsInStock{List<productId> max1000;} 😔
// -StockIncremented/StockDecremented 😔 = not DOMAIN words/ubiquitous language
// -StockDecreased
// -StockUpdated{productId} 😔notification requiring a call back
//=======  😀
// A) StockUpdated{productId, stock} = <CUD Event> moving a data snapshot
// Pro: extensibility (more consumers can listen later) = integration event

// Domain Events:
// B) OutOfStock{productId} + BackInStock{productId} = convenient for catalog
// Pro: less messages
// Cons: consumer-specific ?

// C) ItemSold{productId, quantity} / StockRefilled / StockAdjusted{delta}
//      >>> instead of ItemReturned / ItemDroppedAndSmashedOnTheGround / ItemLostFromWarehouse
// Cons: force consumer has to track old state (past stock)
// Cons: not idempotent = not naturally retryable > consumer has to deduplicate by other means (eg headers)
  // + field private Integer stock;

// D) ItemSold{productId, quantity, stockSnapshot:{items:N, warehouse}} = EventShot
// Pro: consumers don't have to remember past state
// Cons: you leak your internal data

// with a [stateful] event stream processor (KStream)
// you can convert A [or C] to B







  @OneToMany(mappedBy = "product")
  private List<ProductReview> reviews = new ArrayList<>();
}
