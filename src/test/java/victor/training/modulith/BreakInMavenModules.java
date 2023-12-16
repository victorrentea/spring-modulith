package victor.training.modulith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

public class BreakInMavenModules {

  public static final File SRC_MODULE_ROOT = new File("src/main/java/victor/training/modulith");

  public static void main(String[] args) throws IOException {

    List<String> moduleNames = Arrays.stream(SRC_MODULE_ROOT.listFiles())
        .filter(File::isDirectory)
        .map(File::getName)
        .toList();

    for (String moduleName : moduleNames) {
      System.out.println("Creating module " + moduleName);
      File srcFolder = new File(moduleName + "/src/main/java/victor/training/modulith");
      srcFolder.mkdirs();
      File testFolder = new File(moduleName + "/src/test/java/victor/training/modulith");
      testFolder.mkdirs();
      createPom(moduleName);

      new File(SRC_MODULE_ROOT, moduleName).renameTo(new File(srcFolder, moduleName));

      replaceInPomXml("<packaging>jar</packaging>",
          "<packaging>pom</packaging>\n<modules>\n"
          + moduleNames.stream().map(s -> "<module>" + s + "</module>\n").collect(joining())
          + "</modules>");

    }
  }

  private static void replaceInPomXml(String what, String withWhat) throws IOException {
    File parentPomXml = new File("pom.xml");
    String content = new String(Files.readAllBytes(parentPomXml.toPath()), UTF_8);
    content = content.replace(what, withWhat);
    Files.write(parentPomXml.toPath(), content.getBytes(UTF_8));
  }

  private static void createPom(String moduleName) throws IOException {
    Files.writeString(new File(moduleName + "/pom.xml").toPath(), """
        <?xml version="1.0" encoding="UTF-8"?>
        <project>
          <modelVersion>4.0.0</modelVersion>
          <parent>
            <groupId>victor.training</groupId>
            <artifactId>spring-modulith</artifactId>
            <version>1.0</version>
          </parent>
          <artifactId>%s</artifactId>
          <dependencies>
            
          </dependencies>
        </project>
        """.formatted(moduleName));
  }
}
