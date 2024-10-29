package victor.training.modulith.catalog.impl;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

  @ElementCollection
  private Map<String,String> attributes;

  private Double price;

  private Double stars;

  @OneToMany(mappedBy = "product")
  private List<ProductReview> reviews = new ArrayList<>();

//  private Integer stock; // ntot my data. needs to be kept in sync
  private Boolean inStock; // kept in sync with inventory
}
// options to keep this in sync with inventory:
// - catalog-service poll REST call them inventory every 5s/30s/1minute for all productIds in stock
// twist: DB trigger automatically inserting into UPDATES_TO_SEND table to push only the diff
//   Cons: add behavior/processing time in DB
//   alternative: insert in that table from Java code with an 'if'
// - inventory-service DB CDC-es me > K = tailing the DB internal tx log

// - inventory-service publishes an event (K): name / contents
// A) OutOfStock{productId} + BackInStock{productId} = what catalog-service needs
// Cons: these events could be more convenient for catalog-service (listener) than the inventory-service (publisher)
// B) StockAvailabilityChanged{productId, boolean inStock}
// Pro: less types of events

// C) StockUpdated{productId, int stock} = ⭐️Integration Event moving state
// Pro: brings in a snapshot of the stock; best for future
// eg a service that when the stock drops below a threshold orders more from suppliers to refill the stock
// Cons: no intent attached to the state coming in

// D) ⭐️Domain Event = business intent ~ event sourcing
// eg ItemSold{productId, int quantity},
//    ItemReturned{productId, int quantity},
//    StockRefilled{productId, int quantity} +
//    + 5 more
// Pro: fine-grained
// Cons: we have to track the stock on our side because it brings in only deltas
// But, we can transform these events in C as a Kafka compact topic +KStream

// E) EventShot = ItemSold{productId, int quantity, snapshot:{ int newStock}}

