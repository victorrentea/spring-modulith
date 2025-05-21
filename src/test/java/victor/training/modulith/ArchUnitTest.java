package victor.training.modulith;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.dependencies.SliceRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.base.DescribedPredicate.doesNot;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_TESTS;
import static org.assertj.core.api.Assertions.assertThat;

@AnalyzeClasses(packages = "victor.training.modulith",
    importOptions = DoNotIncludeTests.class)
class ArchUnitTest {

  @ArchTest
  public void modules_only_depend_on_internal_apis_of_others(JavaClasses classes) { // TODO
    var rule = SlicesRuleDefinition.slices()
        .matching("victor.training.modulith.(*)..")
        .should().notDependOnEachOther()

        // Exception: it's allowed to depend on any class in any top-level package = exposed internal api
        .ignoreDependency(alwaysTrue(), resideInAnyPackage("victor.training.modulith.*"))

        // ok to depend on any class in 'shared' or any of its sub-packages
        .ignoreDependency(alwaysTrue(), resideInAnyPackage("victor.training.modulith.shared.."));

    // crash on any violation
    rule.check(classes);

    // measure distance from ideal
//    assertThat(rule.evaluate(classes).getFailureReport().getDetails()).hasSize(0);

    // record initial violations in /src/test/resources/archunit
    // ❌ FAILS for new violations 🔼
    // ✅ PASSES for same known violations 🟰
    // ✅ PASSES for less violations 🔽, and auto-updates the file
//    FreezingArchRule.freeze(rule).check(classes);
  }

  @ArchTest
  public void no_cycles_between_modules(JavaClasses classes) {
    var cycles = SlicesRuleDefinition.slices()
        .matching("victor.training.modulith.(*)..")
        .should().beFreeOfCycles()
        .evaluate(classes).getFailureReport().getDetails();

    assertThat(cycles).hasSize(0);
  }

  @ArchTest
  public void module_internal_apis_are_independent(JavaClasses classes) {
    SlicesRuleDefinition.slices()
        .matching("victor.training.modulith.(*)") // root package of all slices
        .should().notDependOnEachOther()
        .ignoreDependency(alwaysTrue(), resideInAnyPackage("victor.training.modulith.shared"))
        .check(classes);
  }

//  @ArchTest // TODO Use when the APIs are moved to shared
  public void module_internal_apis_are_independent_in_shared(JavaClasses classes) {
    SlicesRuleDefinition.slices()
        .matching("victor.training.modulith.shared.api.(*)..*")
        .should().notDependOnEachOther().check(classes);
  }
}
