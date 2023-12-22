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
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitClone {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitClone.class);
    private static final File SSH_DIR = new File(FS.DETECTED.userHome(), "/.ssh");
    private static final File GITCONFIG_FILE = new File(FS.detect().userHome(), ".gitconfig");

    public void cloneRepository(File whereToClone, Set<String> repos) {
        // FIXME This line doesn't make any sense to me... I don't know why i left it here...
        SshSessionFactory.setInstance(getSessionFactory());

        // FIXME This may fail if SSH keys are not set up properly for github.com!
        repos.forEach(repoUrl -> {
            String repoName = repositoryNameExtractor(repoUrl);
            File finalPath = new File(whereToClone, repoName);

            // then clone
            // NOTE:
            LOGGER.info("Cloning from " + repoUrl + " to " + finalPath);

            try {
                Git result = Git.cloneRepository()
                        .setURI(repoUrl)
                        .setDirectory(finalPath)
                        .call();
                LOGGER.info("Having repository: " + result.getRepository().getDirectory());
            } catch (JGitInternalException | GitAPIException e) {
                LOGGER.error(e.getLocalizedMessage());
            }
        });
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

// For further queries:

// Full JGIT cookbook
// https://github.com/centic9/jgit-cookbook/tree/master/src/main/java/org/dstadler/jgit/

// Pode ser necessario copiar a configuracao global do git para a pasta /etc exatamente como à seguir:
// sudo cp .gitconfig /etc/gitconfig
//
// se for necessario modificar o acesso ao arquivo use algo como a seguir:
// sudo chmod -u=rwx -g=rwx -o=rwx gitconfig

// o mais useful stackoverflow da vida!!!
// https://stackoverflow.com/questions/67767455/setting-ssh-keys-to-use-with-jgit-with-ssh-from-apache-sshd