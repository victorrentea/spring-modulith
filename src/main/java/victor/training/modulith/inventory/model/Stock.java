package victor.training.modulith.inventory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.AbstractAggregateRoot;
import victor.training.modulith.catalog.impl.Product;

@Getter
@ToString
@Entity
@Table(schema = "inventory")
@SequenceGenerator(name = "stock_seq", schema = "inventory")
public class Stock {
  @Id
  @GeneratedValue(generator = "stock_seq")
  private Long id;

//  @ManyToOne
//  private Product product;
  //var s=stockRepo.findById(id) ⇒ 1 SELECT STOCK.*, PRODUCT.* FROM STOCK JOIN PRODUCT

// A) just their id ⭐️⭐️⭐️⭐️⭐️
  // ⇒ to get the required fields you'll have to call catalog internal API getProducyById
  // 🙁 Perf hit: +1 SELECT
  // 🙁 More Design: ProductDto, Mapper, more code to write/gen,test, read+maintain❤️❤️❤️
  private Long productId;

  // + Foreign-Key-ed to CATALOG.PRODUCT.ID
  // 😊 strongly consistent ❤️: I will never see a STOCK row associated with a product that doesn't exist
  // 🙁 temporal coupling => when adding a new product to the app, I have to add the rows in different tables in a certain order
  // 🙁 DB incremental scripts of modules cannot be independent (flyway/liquibase for java in fluent migrator(C#)
  //    ⇒ consolidated DB incremental script trail
  // 🙁 when testing the stock module, I will have to first insert a row in a PRODUCT table of another team,
  //    just in order to be able to associate stock to that product on my side
  // 👑👑⭐️⭐️ most of your testing should be at the module level (not unit, not e2e)

  // Need for FK:
  //  when you insert a new row in a table, you could check that in another table, in another API, a product with some ID exists
  //    if (apiCall.get(productId) == 404) throw
  // FKs help preserve this existence, this constraint in time ⭐️⭐️

  // Imagine a world without FKs => what chance do you have?
  // Logical/Soft Deletion: +column 'IS_ACTIVE' = {1|0}
  // if there was a foreign key between product and stock, if the foreign key is gone,
  // the only chance would be to say that products are never physically deleted,
  // but only marked as logically deleted
  // Once catalog gave you a productId, that productId becomes immortal (never returns 404 at GET)🤞


// B)copying interesting attrs from the source
  // 😊 -1 SELECT/call/REST
  // 🙁 replicating data - how to maintain @ update on source => kept in sync via events
// private ProductProjection{3 interesting attrs}

  // WHEN to copy data, not just keep their ID
  // ✅ attrs are immutable
  // ✅ source table is huge (200 columns😱😱) for faster SQL access
  // ✅ I need it very often, to avoid an expensive SELECT/GET (imagine in a for loop)
  // ✅ if inventory becomes a standalone deploy tomorrow, it will be ⭐️fault-tolerant to catalog going down

  @NotNull
  private Integer items = 0;

  public Stock add(int itemsAdded) {
    if (itemsAdded <= 0) {
      throw new IllegalArgumentException("Negative: " + itemsAdded);
    }
    items += itemsAdded;
    return this;
  }

  public void remove(Integer itemsRemoved) {
    if (itemsRemoved <= 0) {
      throw new IllegalArgumentException("Must substract a positive number: " + itemsRemoved);
    }
    if (itemsRemoved > items) {
      throw new IllegalArgumentException("Not enough stock to remove: " + itemsRemoved);
    }
    items -= itemsRemoved;
  }
}
