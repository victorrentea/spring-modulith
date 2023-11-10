package victor.training.modulith;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static org.springframework.modulith.core.ApplicationModules.*;

class ArchitectureTest {
	public static final ApplicationModules modules = of(ModulithApp.class,
			resideInAnyPackage(
					"victor.training.modulith.boot",
					"victor.training.modulith.shared"));

	@Test
	void verifyModularity() {
		modules.verify();
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
