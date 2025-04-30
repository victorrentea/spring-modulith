package victor.training.modulith;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.base.DescribedPredicate.doesNot;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_TESTS;
import static org.assertj.core.api.Assertions.assertThat;

class ArchUnitTest {
  private static final JavaClasses PROJECT_CLASSES = new ClassFileImporter()
      .withImportOption(DO_NOT_INCLUDE_TESTS)
      .importPackages("victor.training.modulith")
      .that(doesNot(resideInAnyPackage("victor.training.modulith.e2e")))
      ;

  @Test
  public void respectEncapsulation() { // TODO
    var slices = SlicesRuleDefinition.slices()
        .matching("victor.training.modulith.(*)..");
    var violations = slices.should().notDependOnEachOther()
        .ignoreDependency(alwaysTrue(), resideInAnyPackage("victor.training.modulith.*"))
        .ignoreDependency(alwaysTrue(), resideInAnyPackage("victor.training.modulith.shared.."))
        .evaluate(PROJECT_CLASSES).getFailureReport().getDetails();

    assertThat(violations).hasSize(0); // end 🎉
  }

  @Test
  public void noCycles() {
    var slices = SlicesRuleDefinition.slices()
        .matching("victor.training.modulith.(*)..*");
    var cycles = slices.should().beFreeOfCycles()
        .evaluate(PROJECT_CLASSES).getFailureReport().getDetails();

    // assertThat(cycles).hasSize(3); // starting point of migration
    // assertThat(cycles).hasSize(3); // next quarter
    assertThat(cycles).hasSize(0); // end 🎉
  }

  @Test
  public void moduleInternalApisAreIndependent() {
    SlicesRuleDefinition.slices()
        .matching("victor.training.modulith.(*)") // root package of all slices
        .should().notDependOnEachOther()
        .ignoreDependency(alwaysTrue(), resideInAnyPackage("victor.training.modulith.shared"))
        .check(new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("victor.training.modulith"));

  }

  @Test
  @Disabled("For after the APIs are moved to shared")
  public void moduleInternalApisInSharedAreIndependent() {
    SlicesRuleDefinition.slices()
        .matching("victor.training.modulith.shared.api.(*)..*")
        .should().notDependOnEachOther().check(PROJECT_CLASSES);
  }

}
