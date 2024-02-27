package victor.training.modulith.customer.impl;

import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
  @Id
  private String id;
  private String fullName;
  private String address;
  private String email;
}
