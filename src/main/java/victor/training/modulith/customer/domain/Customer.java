package victor.training.modulith.customer.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
