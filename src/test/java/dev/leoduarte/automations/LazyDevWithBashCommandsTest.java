package dev.leoduarte.automations;

import org.eclipse.jgit.util.FS;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class LazyDevWithBashCommandsTest {

    private final File GIT_DIRECTORY = new File(FS.DETECTED.userHome(), "/Git");
    private final LazyDevWithBashCommands underTest = new LazyDevWithBashCommands();

    @Test
    @DisplayName("mvn clean")
    @Disabled("For manually usage purpose")
    void mvnClean() throws IOException {
        underTest.mvnExecuteCommands(GIT_DIRECTORY, "clean");
    }

    @Test
    @DisplayName("mvn clean verify")
    @Disabled("For manually usage purpose")
    void mvnCleanVerify() throws IOException {
        underTest.mvnExecuteCommands(GIT_DIRECTORY, "clean", "verify");
    }

    @Test
    @DisplayName("mvn clean package")
    @Disabled("For manually usage purpose")
    void mvnCleanPackage() throws IOException {
        underTest.mvnExecuteCommands(GIT_DIRECTORY, "clean", "package", "-DskipTests", "-DskipITs");
    }

    @Test
    @DisplayName("mvn clean install")
    @Disabled("For manually usage purpose")
    void mvnCleanInstall() throws IOException {
        underTest.mvnExecuteCommands(GIT_DIRECTORY, "clean", "install", "-DskipTests", "-DskipITs");
    }

    @Test
    @DisplayName("java --version")
    @Disabled("For manually usage purpose")
    void getJavaVersion() throws IOException {
        underTest.getJavaVersion();
    }

    @Test
    @DisplayName("mvn -v")
    @Disabled("For manually usage purpose")
    void getMavenVersion() throws IOException {
        underTest.getMavenVersion();
    }

    @Test
    @DisplayName("printenv")
    @Disabled("For manually usage purpose")
    void printEnv() {
        underTest.printEnv();
    }
}