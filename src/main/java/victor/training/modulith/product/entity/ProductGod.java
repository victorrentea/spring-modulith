package victor.training.modulith.product.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

// original God class
public class ProductGod {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private String description;

  private Double price;

  private int stock;

  private int weight;

}

