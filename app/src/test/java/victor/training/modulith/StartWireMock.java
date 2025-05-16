package victor.training.modulith;

import com.github.tomakehurst.wiremock.standalone.WireMockServerRunner;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class StartWireMock {
  public static void main(String[] args) throws IOException {
    File rootFolder = new File(".", "src/test/resources/wiremock");
    File mappingsFolder = new File(rootFolder, "mappings");
    System.out.println("*.json mappings stubs expected at " + mappingsFolder.getAbsolutePath());
    var jsonFound = Arrays.stream(mappingsFolder.listFiles()).filter(File::isFile).anyMatch(f -> f.getName().endsWith(".json"));
    if (!jsonFound) {
      throw new IllegalStateException("No *.json mappings stubs found at " + mappingsFolder.getAbsolutePath());
    }

    new WireMockServerRunner().run(
            "--port", "9999",
            "--root-dir", rootFolder.getAbsolutePath(),
            "--global-response-templating", // UUID
            "--async-response-enabled=true" // enable Wiremock to not bottleneck on heavy load
    );
  }
}
