package victor.training.modulith.catalog.impl;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data // sorry
public class Product {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private String description;

  private Double price;

  private Double stars;

  @OneToMany(mappedBy = "product")
  private List<ProductReview> reviews = new ArrayList<>();

  private Boolean inStock; // TEMA
  // NU CUMVA cu triggeri sau proceduri. Nu mai e la moda.
  // nu e in Java, nu e testabil, misterios si greu de inteles.

  // daca ai de update 1M+ recorduri in minute, atunci ai voie PL/SQL
}
