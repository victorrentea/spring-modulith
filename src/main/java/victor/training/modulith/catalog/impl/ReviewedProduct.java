package victor.training.modulith.catalog.impl;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//@Entity
//@Table(schema = "catalog")
//@Data
//public class ReviewedProduct {
//  @Id
//  @GeneratedValue
//  private Long id;
//
//  @NotNull
//  @Setter // + FK to PRODUCT.ID
//  private Long productId;
//
//  private Double stars;
//
//  @OneToMany(mappedBy = "reviewedProduct")
//  private List<ProductReview> reviews = new ArrayList<>();
//}
