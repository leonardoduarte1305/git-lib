package dev.leoduarte.automations;

import org.eclipse.jgit.util.FS;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

class GitCloneTest {

    private static final File ROOT_PLACE_TO_CLONE = new File(FS.DETECTED.userHome(), "/Git");
    private final GitClone underTest = new GitClone();

    @Test
    @DisplayName("git clone")
    @Disabled("For manually usage purpose")
    void gitClone() {
        Set<String> repos = new HashSet<>();
        repos.add("git@github.com:leonardoduarte1305/kafka-setup.git");
        repos.add("git@github.com:leonardoduarte1305/kafka-setup123.git");
        repos.add("git@github.com:leonardoduarte1305/kubernetes.git");

        underTest.cloneRepository(ROOT_PLACE_TO_CLONE, repos);
    }
}