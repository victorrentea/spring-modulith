package victor.training.modulith;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;

class ArchitectureTest {
	public static final DescribedPredicate<JavaClass> IGNORED_MODULES =
			resideInAnyPackage( "victor.training.modulith.shared");
	public static final ApplicationModules modules =
			ApplicationModules.of(ModulithApp.class, IGNORED_MODULES);

	@Test
	void verifyModularity() {
		// Checks modules only use each other's PUBLIC API (not internals)
		// + no cycles
		// Note: this test still runs after break down in Maven modules
		modules.verify(); // provided by org.springframework.modulith:spring-modulith-starter-core
	}

	@Test
	void writeDocumentationSnippets() {
		new Documenter(modules)
				.writeModulesAsPlantUml()
				.writeIndividualModulesAsPlantUml();
	}

	@Test
	void renderAsciidoc() throws Exception {
		var canvasOptions = Documenter.CanvasOptions.defaults();

		var docOptions = Documenter.DiagramOptions.defaults()
				.withStyle(Documenter.DiagramOptions.DiagramStyle.UML);

		new Documenter(modules) //
				.writeDocumentation(docOptions, canvasOptions);
	}
}
