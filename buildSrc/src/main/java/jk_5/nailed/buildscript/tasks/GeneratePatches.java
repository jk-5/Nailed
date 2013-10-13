package jk_5.nailed.buildscript.tasks;

import com.cloudbees.diff.Diff;
import com.cloudbees.diff.PatchException;
import com.google.common.io.Files;
import lombok.Getter;
import lombok.Setter;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class GeneratePatches extends DefaultTask {
    @InputDirectory
    @Getter
    @Setter
    File patchDir;

    @InputDirectory
    @Getter
    @Setter
    File changedDir;

    @InputDirectory
    @Getter
    @Setter
    File originalDir;

    @TaskAction
    public void doTask() throws IOException, PatchException {
        patchDir.mkdirs();

        // fix and create patches.
        processDir(originalDir);
    }

    public void processDir(File dir) throws IOException {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                processDir(file);
            } else if (file.getPath().endsWith(".java")) {
                processFile(file);
            }
        }
    }

    public void processFile(File file) throws IOException {
        getLogger().debug("Original File: " + file);
        String relative = file.getAbsolutePath().substring(originalDir.getAbsolutePath().length()).replace('\\', '/');

        File patchFile = new File(patchDir, relative + ".patch");
        File changedFile = new File(changedDir, relative);

        getLogger().debug("Changed File: " + file);

        Diff diff = Diff.diff(file, changedFile, true);

        if (diff.isEmpty()) {
            String unidiff = diff.toUnifiedDiff(relative, relative, Files.newReader(file, Charset.defaultCharset()), Files.newReader(changedFile, Charset.defaultCharset()), 3);

            patchFile.getParentFile().mkdirs();
            Files.touch(patchFile);
            Files.write(unidiff, patchFile, Charset.defaultCharset());
        }
    }
}
