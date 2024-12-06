package victor.training.modulith;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static org.assertj.core.api.Assertions.assertThat;

class ArchUnitTest {
  private static final JavaClasses PROJECT_CLASSES = new ClassFileImporter()
      .importPackages("victor.training.modulith");

  @Test
  public void noCycles() {
    var slices = SlicesRuleDefinition.slices()
        .matching("victor.training.modulith.(*).*");
    var cycles = slices.should().beFreeOfCycles()
        .evaluate(PROJECT_CLASSES).getFailureReport().getDetails();

    // assertThat(cycles).hasSize(3); // starting point of migration
    // assertThat(cycles).hasSize(3); // next quarter
    assertThat(cycles).hasSize(0); // end 🎉
  }

  @Test
  public void respectEncapsulation() { // TODO
    var slices = SlicesRuleDefinition.slices()
        .matching("victor.training.modulith.(*)..");
    var violations = slices.should().notDependOnEachOther()
        .ignoreDependency(alwaysTrue(), resideInAnyPackage("victor.training.modulith.*"))
        .ignoreDependency(alwaysTrue(), resideInAnyPackage("victor.training.modulith.shared"))
        .evaluate(PROJECT_CLASSES).getFailureReport().getDetails();

    assertThat(violations).hasSize(0); // end 🎉
  }

  @Test
  public void moduleInternalApisAreIndependent() {
    SlicesRuleDefinition.slices()
        .matching("victor.training.modulith.(*)")
        .should().notDependOnEachOther()
        .ignoreDependency(alwaysTrue(), resideInAnyPackage("victor.training.modulith.shared"))
        .check(PROJECT_CLASSES);
  }

  @Test
  @Disabled("For after the APIs are moved to shared")
  public void sharedApiIndependent() {
    SlicesRuleDefinition.slices()
        .matching("victor.training.modulith.shared.api.(*)..*")
        .should().notDependOnEachOther().check(PROJECT_CLASSES);
  }

}
