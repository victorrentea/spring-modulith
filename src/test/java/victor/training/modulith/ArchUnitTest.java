package victor.training.modulith;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.dependencies.SliceRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static org.assertj.core.api.Assertions.assertThat;

class ArchUnitTest {
  private JavaClasses classes = new ClassFileImporter()
      .importPackages("victor.training.modulith");


  @Test
  public void noCyclesBetweenModules() {
    SliceRule rule = SlicesRuleDefinition.slices()
        .matching("victor.training.modulith.(*)")
        .should().beFreeOfCycles();

    // Stage 1. Progressive decoupling phase: lower this number every sprint
    List<String> violations = rule.evaluate(classes).getFailureReport().getDetails();
    assertThat(violations).hasSizeLessThan(123); // starting point after moving classes around
    assertThat(violations).hasSize(0); // 6 months from now

    // Stage 2. Maintenance phase: fail test at any violation
    rule.check(classes);
  }

  @Test
  public void moduleInternalApisAreIndependent() {
    SlicesRuleDefinition.slices()
        .matching("victor.training.modulith.(*)")
        .should().notDependOnEachOther()
        .ignoreDependency(alwaysTrue(), resideInAnyPackage("victor.training.modulith.shared"))
        .check(classes);
  }

  @Test
  @Disabled("For after the APIs are moved to shared")
  public void sharedApiIndependent() {
    SlicesRuleDefinition.slices()
        .matching("victor.training.modulith.shared.api.(*)..*")
        .should().notDependOnEachOther().check(classes);
  }

}
