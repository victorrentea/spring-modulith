package victor.training.modulith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@EnableFeignClients
@SpringBootApplication(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class) // allow classes with same name in different packages, eg "InitialData"
@ConfigurationPropertiesScan
public class ModulithApp {

	public static void main(String[] args) {
		SpringApplication.run(ModulithApp.class, args);
	}

}
