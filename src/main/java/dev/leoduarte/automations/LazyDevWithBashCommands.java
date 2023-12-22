package dev.leoduarte.automations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class LazyDevWithBashCommands {

    // FIXME Every command will be executed recursively into the main directory, so the purpose is to clean the projects
    //  or to automatically download all the Maven dependencies for all projects

    // FIXME Do not forget to configure the servers into settings.xml to access the private remote Maven registry, if necessary.

    // For further queries:
    // https://www.baeldung.com/java-lang-processbuilder-api

    private static final Logger LOGGER = LoggerFactory.getLogger(LazyDevWithBashCommands.class);
    private static final String COMMAND_BIN_BASH = "/bin/bash";
    private static final String COMMAND_MINUS_C = "-c";

    public void mvnExecuteCommands(File whereToExecute, String... commands) throws IOException {
        ProcessBuilder processBuilder = createProcessBuilder(
                whereToExecute,
                COMMAND_BIN_BASH,
                COMMAND_MINUS_C,
                getRecursiveCommandWithMvnPhases(commands));

        Process process = processBuilder.start();
        printProcessResult(process);
        waitProcess(process);
    }

    private static String getRecursiveCommandWithMvnPhases(String... mvnCommands) {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(mvnCommands).forEach(command -> stringBuilder.append(command).append(" "));

        return "find . -name \"pom.xml\" -exec mvn " + stringBuilder + "-f '{}' \\;";
    }

    // Assuming the Java version is available from the PATH variables
    public void getJavaVersion() throws IOException {
        Process process = createAndStartProcess("java", "--version");
        printProcessResult(process);
    }

    // Assuming the Maven version is available from the PATH variables
    public void getMavenVersion() throws IOException {
        Process process = createAndStartProcess("mvn", "-v");
        printProcessResult(process);
    }

    public void printEnv() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        Map<String, String> environment = processBuilder.environment();
        environment.forEach((env, value) -> System.out.println(env + " = " + value));
    }

    private ProcessBuilder createProcessBuilder(File path, String... commands) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(path);
        processBuilder.command(commands);
        processBuilder.inheritIO();
        return processBuilder;
    }

    private void printProcessResult(Process process) {
        InputStream inputStream = getInputStream(process);
        Scanner scanner = getScannerForInputStream(inputStream);
        String result = getResult(scanner);
        printResult(result);
    }

    private void waitProcess(Process process) {
        try {
            int exitCode = process.waitFor();
            printResult("Exit code: " + exitCode);
        } catch (InterruptedException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
    }

    private Process createAndStartProcess(String... commands) throws IOException {
        return new ProcessBuilder(commands).start();
    }

    private InputStream getInputStream(Process process) {
        return process.getInputStream();
    }

    private Scanner getScannerForInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\A");
    }

    private String getResult(Scanner scanner) {
        return scanner.hasNext() ? scanner.next() : "";
    }

    private void printResult(String result) {
        System.out.println(result);
    }
}
