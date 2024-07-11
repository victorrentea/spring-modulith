package victor.training.modulith.customer.impl;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
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
