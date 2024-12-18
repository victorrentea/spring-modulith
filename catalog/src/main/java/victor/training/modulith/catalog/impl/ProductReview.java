package victor.training.modulith.catalog.impl;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Optional;

@Entity
@Data // sorry
public class ProductReview {
  @Id
  @GeneratedValue
  private Long id;
  @ManyToOne
  @ToString.Exclude
  private Product product;
  private String title;
  private String contents;
  private Double stars;
  private LocalDate createdAt;

  public Optional<Double> stars() {
    return Optional.ofNullable(stars);
  }

  public Optional<String> title() {
    return Optional.ofNullable(title);
  }
}