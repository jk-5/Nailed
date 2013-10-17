package jk_5.nailed.buildscript.tasks;

import com.cloudbees.diff.PatchException;
import com.google.common.io.Files;
import lombok.Getter;
import lombok.Setter;
import jk_5.nailed.buildscript.Constants;
import jk_5.nailed.buildscript.patching.ContextualPatch;
import org.gradle.api.DefaultTask;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class PatchTask extends DefaultTask {
    @InputDirectory
    @Getter
    @Setter
    File patchDir;

    @OutputDirectory
    @InputDirectory
    @Getter
    @Setter
    File filesDir;

    private File tempPatchDir = new File(this.getTemporaryDir(), "patches");
    private ArrayList<ContextualPatch> loadedPatches = new ArrayList<ContextualPatch>();

    @TaskAction
    @SuppressWarnings("unused")
    public void doTask() throws IOException, PatchException {
        // fix and create patches.
        processDir(patchDir);

        boolean fuzzed = false;

        // apply patches
        for (ContextualPatch patch : loadedPatches) {
            patch.setAccessC14N(true);

            List<ContextualPatch.PatchReport> errors = patch.patch(false);
            for (ContextualPatch.PatchReport report : errors) {
                if(report.getStatus().isSuccess()){
                    getLogger().info("Patch succeeded: " + report.getTarget());
                }else if (report.getStatus() == ContextualPatch.PatchStatus.Failure) {
                    getLogger().log(LogLevel.ERROR, "Patching failed: " + report.getTarget(), report.getFailure());

                    // now spit the hunks
                    for (ContextualPatch.HunkReport hunk : report.getHunks()) {
                        // catch the failed hunks
                        if (!hunk.getStatus().isSuccess()) {
                            getLogger().error("Hunk %d failed!", hunk.getIndex());
                        }
                    }
                }else if (report.getStatus() == ContextualPatch.PatchStatus.Fuzzed) {
                    getLogger().log(LogLevel.INFO, "Patching fuzzed: " + report.getTarget(), report.getFailure());

                    // set the boolean for later use
                    fuzzed = true;

                    // now spit the hunks
                    for (ContextualPatch.HunkReport hunk : report.getHunks()) {
                        // catch the failed hunks
                        if (!hunk.getStatus().isSuccess()) {
                            getLogger().info("Hunk %d fuzzed %d!", hunk.getIndex(), hunk.getFuzz());
                        }
                    }
                }
            }
        }

        for(File file : this.filesDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".original~");
            }
        })){
            file.delete();
        }
    }

    public void processDir(File dir) throws IOException {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                processDir(file);
            } else if (file.getPath().endsWith(".patch")) {
                processFile(file);
            }
        }
    }

    public void processFile(File file) throws IOException {
        getLogger().debug("Formatting file: " + file);
        String relative = file.getAbsolutePath().substring(patchDir.getAbsolutePath().length());
        File outFile = new File(tempPatchDir, relative);

        String text = Files.toString(file, Charset.defaultCharset());

        // newlines
        text = text.replaceAll("(\r\n|\r|\n)", Constants.NEWLINE);
        text = text.replaceAll("(\\r\\n|\\r|\\n)", Constants.NEWLINE);

        // fixing for the paths.
        text = text.replaceAll("\\.\\./src-base/minecraft/(net/minecraft)", "$1");
        outFile.getParentFile().mkdirs();
        Files.touch(outFile);
        Files.write(text, outFile, Charset.defaultCharset());

        getLogger().info("Loading Patch: " + file);
        loadedPatches.add(ContextualPatch.create(outFile, filesDir));
    }
}
