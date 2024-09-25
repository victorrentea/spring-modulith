package victor.training.modulith.customer.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.catalog.CatalogInternalApi;
import victor.training.modulith.catalog.impl.ProductRepo;

@RestController
@RequiredArgsConstructor
public class GetCustomerApi {
  private final CustomerRepo customerRepo;
//  private final ProductRepo productRepo;

  public record GetCustomerResponse(
      String id,
      String name,
      String address,
      String email
  ) {}

  @GetMapping("customer/{id}")
  public GetCustomerResponse getCustomerById(@PathVariable String id) {
    Customer entity = customerRepo.findById(id).orElseThrow();
    return new GetCustomerResponse(
        entity.id(),
        entity.fullName(),
        entity.address(),
        entity.email());
  }
}
