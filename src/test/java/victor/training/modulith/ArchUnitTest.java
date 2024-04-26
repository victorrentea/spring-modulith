package victor.training.modulith;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.dependencies.SliceRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import com.tngtech.archunit.library.dependencies.syntax.GivenSlices;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ArchUnitTest {
  private JavaClasses classes = new ClassFileImporter().importPackages("victor.training.modulith");

  @Test
  public void sharedApiIndependent() {
    SliceRule sliceRule = SlicesRuleDefinition.slices()
        .matching("victor.training.modulith.shared.api.(*)..*")
        .should().notDependOnEachOther();

    // Stage 1. Progressive decoupling phase: lower this number every sprint
    List<String> violations = sliceRule.evaluate(classes).getFailureReport().getDetails();
    assertThat(violations).hasSizeLessThanOrEqualTo(123); // starting point after moving classes around
    assertThat(violations).hasSizeLessThanOrEqualTo(0); // 6 months from now

    // Stage 2. Maintenance phase: fail test at any violation
    sliceRule.check(classes);
  }

  @Test
  public void noCyclesBetweenModules() {
    SlicesRuleDefinition.slices()
        .matching("victor.training.modulith.(*)..*")
        .should().beFreeOfCycles()
        .check(classes);
  }

}
