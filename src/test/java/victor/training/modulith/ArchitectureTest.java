package victor.training.modulith;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;

class ArchitectureTest {
	public static final DescribedPredicate<JavaClass> IGNORED_MODULES =
			resideInAnyPackage("victor.training.modulith.boot", "victor.training.modulith.shared");
	public static final ApplicationModules modules =
			ApplicationModules.of(ModulithApp.class, IGNORED_MODULES);

	@Test
	void verifyModularity() {
		modules.verify(); // Spring-modulith support https://spring.io/projects/spring-modulith
	}

	@Test
	void writeDocumentationSnippets() {
		new Documenter(modules)
				.writeModulesAsPlantUml()
				.writeIndividualModulesAsPlantUml();
	}

	@Test
	void renderAsciidoc() throws Exception {
		var canvasOptions = Documenter.CanvasOptions.defaults()
				// --> Optionally enable linking of JavaDoc
//				 .withApiBase("https://foobar.something")
				;

		var docOptions = Documenter.DiagramOptions.defaults()
				.withStyle(Documenter.DiagramOptions.DiagramStyle.UML);

		new Documenter(modules) //
				.writeDocumentation(docOptions, canvasOptions);
	}
}
