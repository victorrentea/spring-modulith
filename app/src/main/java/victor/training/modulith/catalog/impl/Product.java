package victor.training.modulith.catalog.impl;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data // sorry
public class Product {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private String description;

  private Double price;

  // there used to be a STOCK field here,
  // but it moved to Stock @Entity when thy broke out
  // the Inventory Module from the Catalog Module

  private boolean available;
}
