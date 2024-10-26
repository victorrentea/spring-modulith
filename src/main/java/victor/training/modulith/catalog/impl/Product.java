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

  @OneToMany(mappedBy = "product")
  private List<ProductReview> reviews = new ArrayList<>();

//  private Integer stock;
  private Boolean inStock; // replic minim necesar

}
// cum se numeste evenimentul(ele)=un fapt (verb la trecut) si ce contin ele?
// A) OutOfStock{productId}/BackInStock{productId} = pasate prin mem event in cadrul unui modulith Spring
// COns: 2 tipuri de eventuri? pe bune?!? overkill

// B) StockStatusChanged{productId, boolean available} = integration event
// productRepo.fbi(id).inStock = event.available;
// mai putine event / topic vs C
// -------------------------------------------------------
// C) StockUpdated{productId, count} = external integration events: publicata pe topice publice
//   reusable de multe alte sisteme in viitor
//   - data lake / BI
//   - warning-to-refill-stock-service cand count<10

// D) dpdv inventar = Domain [Internal] Events: events-sourced/insert in AUDIT...
// ❌StockDecrementedEvent stock--  nu e business-language
//   StockDecreasedEvent stock--
//   ArticleSold - e in ubiquitous language al echipei inventory
//   ProductSold{productId, count, dinCeWarehouse} + ProductShipmentReceived{productId, count}+productReturned+StockAdjusted(cateAmNumarat)
// Catalog e obligat sa pastreze valorile vechi
