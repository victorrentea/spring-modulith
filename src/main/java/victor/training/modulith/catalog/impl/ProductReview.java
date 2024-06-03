package victor.training.modulith.catalog.impl;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Optional;

@Entity
@Data // sorry
@SequenceGenerator(name = "review_seq", schema = "catalog")
public class ProductReview {
  @Id
  @GeneratedValue(generator = "review_seq")
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
