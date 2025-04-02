package victor.training.modulith.customer.impl;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CreateCustomerApi {
  private final CustomerRepo customerRepo;

  public record CreateCustomerRequest(
      @NotBlank
      String name,
      String address,
      @Email
      @NotNull
      String email
  ) {}

  @PostMapping("customer")
  public String call(@RequestBody @Validated CreateCustomerRequest request) {
    Customer entity = new Customer()
        .id(UUID.randomUUID().toString())
        .fullName(request.name())
        .address(request.address())
        .email(request.email());
    customerRepo.save(entity);
    return entity.id();
  }
}
