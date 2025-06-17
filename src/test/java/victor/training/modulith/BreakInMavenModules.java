package victor.training.modulith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

public class BreakInMavenModules {

  public static void main(String[] args) throws IOException {

    System.out.println("Did you optimized imports for all classes? (right click src/main/java) [y/n]");
    if (!new Scanner(System.in).nextLine().equalsIgnoreCase("y")) {
      System.out.println("Do that first!");
      return;
    }

    List<String> moduleNames = Arrays.stream(new File("src/main/java/victor/training/modulith").listFiles())
        .filter(File::isDirectory)
        .map(File::getName)
        .collect(Collectors.toList());

    String appDependencies = moduleNames.stream().map("""
            <dependency>
              <groupId>victor.training</groupId>
              <artifactId>%s</artifactId>
              <version>1.0</version>
            </dependency>"""::formatted).collect(joining());
    moduleNames.add("app");
    boolean b;
    for (String moduleName : moduleNames) {
      if (new File(moduleName).isDirectory()) {
        System.out.println("Module " + moduleName + " already exists. Deleting it");
        deleteDirectory(new File(moduleName));
      }
      File srcFolder = new File(moduleName + "/src/main/java/victor/training/modulith");
      File testFolder = new File(moduleName + "/src/test/java/victor/training/modulith");
      System.out.println("Creating module " + moduleName + "\nSRC: " + srcFolder + "\nTEST: " + testFolder);
      System.out.println("mkdir src:" + srcFolder.mkdirs());
      System.out.println("mkdir test:" + testFolder.mkdirs());

      b = new File(new File("src/main/java/victor/training/modulith"), moduleName).renameTo(new File(srcFolder, moduleName));
      System.out.println("Moved src/main: " + b);
      new File(new File("src/test/java/victor/training/modulith"), moduleName).renameTo(new File(testFolder, moduleName));
      System.out.println("Moved src/test: " + b);


      String dependencies = switch (moduleName) {
        case "app" -> appDependencies;
        case "shared" -> "";
        default -> """
            <dependency>
                <groupId>victor.training</groupId>
                <artifactId>shared</artifactId>
                <version>1.0</version>
              </dependency>
            """;
      };

      createPom(moduleName, dependencies);
    }
    replaceInPomXml("<packaging>jar</packaging>",
        "<packaging>pom</packaging>\n<modules>\n"
        + moduleNames.stream().map(s -> "<module>" + s + "</module>\n").collect(joining())
        + "</modules>");

    deleteDirectory(new File("app/src"));
    System.out.println(new File("src").renameTo(new File("app/src")));
  }
  static boolean deleteDirectory(File directoryToBeDeleted) {
    File[] allContents = directoryToBeDeleted.listFiles();
    if (allContents != null) {
      for (File file : allContents) {
        deleteDirectory(file);
      }
    }
    return directoryToBeDeleted.delete();
  }

  private static void replaceInPomXml(String what, String withWhat) throws IOException {
    File parentPomXml = new File("pom.xml");
    String content = new String(Files.readAllBytes(parentPomXml.toPath()), UTF_8);
    content = content.replace(what, withWhat);
    Files.write(parentPomXml.toPath(), content.getBytes(UTF_8));
  }

  private static void createPom(String moduleName, String dependencies) throws IOException {
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
            %s
          </dependencies>
        </project>
        """.formatted(moduleName, dependencies));
  }
}
