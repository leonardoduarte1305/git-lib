package dev.leoduarte.automations;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.sshd.SshdSessionFactoryBuilder;
import org.eclipse.jgit.util.FS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitClone {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitClone.class);
    private static final File SSH_DIR = new File(FS.DETECTED.userHome(), "/.ssh");
    private static final File GITCONFIG_FILE = new File(FS.detect().userHome(), ".gitconfig");
    private static final File ROOT_PLACE_TO_CLONE = new File(FS.DETECTED.userHome(), "/Git");

    public static void main(String[] args) {
        Set<String> repos = new HashSet<>();
        repos.add("git@github.com:leonardoduarte1305/kafka-setup.git");
        repos.add("git@github.com:leonardoduarte1305/kafka-setup123.git");
        repos.add("git@github.com:leonardoduarte1305/kubernetes.git");

        repos.forEach(repository -> {
            try {
                cloneRepository(repository);
            } catch (IOException | GitAPIException e) {
                LOGGER.error(e.getLocalizedMessage());
            }
        });
    }

    private static void cloneRepository(String repoUrl) throws IOException, GitAPIException {
        SshSessionFactory.setInstance(getSessionFactory());

        String repoName = repositoryNameExtractor(repoUrl);
        File finalPath = new File(GitClone.ROOT_PLACE_TO_CLONE, repoName);

        // then clone
        // NOTE: This may fail if SSH keys are not set up properly for github.com!
        LOGGER.info("Cloning from " + repoUrl + " to " + finalPath);

        try {
            Git result = Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(finalPath)
                    .call();
            LOGGER.info("Having repository: " + result.getRepository().getDirectory());
        } catch (JGitInternalException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
    }

    private static SshSessionFactory getSessionFactory() {
        return new SshdSessionFactoryBuilder()
                .setPreferredAuthentications("publickey")
                .setConfigFile(file -> GITCONFIG_FILE)
                .setHomeDirectory(FS.DETECTED.userHome())
                .setSshDirectory(SSH_DIR)
                .build(null);
    }

    private static String repositoryNameExtractor(String repoUrl) {
        Pattern pattern = Pattern.compile("[:/](\\w[\\w.-]*)\\.git$");
        Matcher matcher = pattern.matcher(repoUrl);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }
}

// Full cookbook
// https://github.com/centic9/jgit-cookbook/tree/master/src/main/java/org/dstadler/jgit/

// Pode ser ncessario copiar a configuracao global do git pasta /etc exatamente como à seguir:
// sudo cp .gitconfig /etc/gitconfig
//
// se for necessario modificar o acesso ao arquivo use algo como a seguir:
// sudo chmod -u=rwx -g=rwx -o=rwx gitconfig

// o mais useful stackoverflow da vida!!!
// https://stackoverflow.com/questions/67767455/setting-ssh-keys-to-use-with-jgit-with-ssh-from-apache-sshd