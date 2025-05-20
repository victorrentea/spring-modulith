package victor.training.modulith;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.web.client.RestClient;

@EnableFeignClients
@SpringBootApplication(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class) // allow classes with same name in different packages, eg "InitialData"
@ConfigurationPropertiesScan
public class ModulithApp {

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(ModulithApp.class);
		builder.profiles("local");
		builder.run(args);
	}
	@Bean
	public RestClient restClient() {
		return RestClient.builder().build();
	}

}
