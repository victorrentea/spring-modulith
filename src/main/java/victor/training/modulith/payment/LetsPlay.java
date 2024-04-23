package victor.training.modulith.payment;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class LetsPlay
{
  public static void main(String[] args) {
    X x = new X("Hello");
    System.out.println(x);
//    X y = new X(null);
  }
}
record X(@NotNull String s) {
  public X(String s) {
    this.s=s;//horror
    Set<ConstraintViolation<X>> list = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
    System.out.println(list);
  }

}