package victor.training.modulith;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

import static org.springframework.modulith.core.ApplicationModules.*;

class ArchitectureTest {
	public static final ApplicationModules modules = of(ModulithApp.class);

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
}
