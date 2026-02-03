package victor.training.modulith.customer.impl;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "customer")
public class Customer {
  @Id
  private String id;
  private String fullName;
  private String address;
  private String email;
}
