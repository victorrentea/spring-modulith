package victor.training.modulith;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;

// provided by org.springframework.modulith:spring-modulith-starter-core
class ArchSpringModulithTest {
	public static final DescribedPredicate<JavaClass> IGNORING_SHARED_MODULE =
			resideInAnyPackage("victor.training.modulith.shared..");
	public static final ApplicationModules modules = ApplicationModules.of(ModulithApp.class);

	@Test
	void encapsulated_and_withoutCycles() {
		// 1. modules only access each others' root package (or explicitly allowed packages)
		// 2. no cycles between modules
		modules.verify();
		// Note: this test runs even after split in Maven modules
	}

	@Test
	void generate_module_dependencies_diagrams() {
		new Documenter(modules)
				.writeModulesAsPlantUml()
				.writeIndividualModulesAsPlantUml();
	}

	@Test
	void generate_asciidoc() {
		var canvasOptions = Documenter.CanvasOptions.defaults();

		var docOptions = Documenter.DiagramOptions.defaults()
				.withStyle(Documenter.DiagramOptions.DiagramStyle.UML);

		new Documenter(modules) //
				.writeDocumentation(docOptions, canvasOptions);
	}

}
