package victor.training.modulith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ModulithApp {

	public static void main(String[] args) {
		SpringApplication.run(ModulithApp.class, args);
	}

}
