package jk_5.nailed.buildscript.tasks;

import com.beust.jcommander.internal.Maps;
import com.github.abrarsyed.jastyle.ASFormatter;
import com.github.abrarsyed.jastyle.OptParser;
import com.google.common.base.Joiner;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import groovy.lang.Closure;
import lombok.Getter;
import lombok.Setter;
import jk_5.nailed.buildscript.Constants;
import jk_5.nailed.buildscript.patching.ContextualPatch;
import jk_5.nailed.buildscript.sourcemanip.FFPatcher;
import jk_5.nailed.buildscript.sourcemanip.McpCleanup;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.JavaExecSpec;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class DecompileTask extends CachedTask {
    @Getter
    @Setter
    @InputFile
    private File inJar;

    @Getter
    @Setter
    @InputFile
    private File fernFlower;

    @Getter
    @Setter
    @InputFile
    private File patch;

    @Getter
    @Setter
    @InputFile
    private File astyleConfig;

    @Getter
    @Setter
    @OutputFile
    @Cached
    private File outZip;

    private HashMap<String, String> sourceMap = new HashMap<String, String>();
    private HashMap<String, byte[]> resourceMap = new HashMap<String, byte[]>();

    /**
     * This method outputs to the cleanSrc
     */
    @TaskAction
    protected void doMCPStuff() throws Throwable {
        // define files.
        File temp = new File(getTemporaryDir(), inJar.getName());

        getLogger().info("Decompiling Jar");
        decompile(inJar, getTemporaryDir(), fernFlower);

        getLogger().info("Loading decompiled jar");
        readJarAndFix(temp);

        getLogger().info("Applying MCP patches");
        applyMcpPatches(patch);

        getLogger().info("Cleaning source");
        applyMcpCleanup(astyleConfig);

        getLogger().info("Saving Jar");
        saveJar(outZip);
    }

    private void decompile(final File inJar, final File outJar, final File fernFlower) {
        getProject().javaexec(new Closure(this) {

            public Object call() {
                JavaExecSpec exec = (JavaExecSpec) getDelegate();

                exec.args(
                        fernFlower.getAbsolutePath(),
                        "-din=0",
                        "-rbr=0",
                        "-dgs=1",
                        "-asc=1",
                        "-log=ERROR",
                        inJar.getAbsolutePath(),
                        outJar.getAbsolutePath()
                );

                exec.setMain("-jar");
                exec.setWorkingDir(fernFlower.getParentFile());

                exec.classpath(Constants.getClassPath());

                exec.setStandardOutput(Constants.getNullStream());

                return exec;
            }

            public Object call(Object obj) {
                return call();
            }
        });
    }

    private void readJarAndFix(final File jar) throws IOException {
        // begin reading jar
        final ZipInputStream zin = new ZipInputStream(new FileInputStream(jar));
        ZipEntry entry = null;
        String fileStr;

        while ((entry = zin.getNextEntry()) != null) {
            // no META or dirs. wel take care of dirs later.
            if (entry.getName().contains("META-INF") || entry.getName().startsWith("argo") || entry.getName().startsWith("com") || entry.getName().startsWith("org")) {
                continue;
            }

            // resources or directories.
            if (entry.isDirectory() || !entry.getName().endsWith(".java")) {
                resourceMap.put(entry.getName(), ByteStreams.toByteArray(zin));
            } else {
                // source!
                fileStr = new String(ByteStreams.toByteArray(zin), Charset.defaultCharset());

                // fix
                fileStr = FFPatcher.processFile(new File(entry.getName()).getName(), fileStr);

                sourceMap.put("minecraft_server/" + entry.getName(), fileStr);
            }
        }

        zin.close();
    }

    private void applyMcpPatches(File patchFile) throws Throwable {
        ContextualPatch patch = ContextualPatch.create(Files.toString(patchFile, Charset.defaultCharset()), new ContextProvider(sourceMap));

        boolean fuzzed = false;

        List<ContextualPatch.PatchReport> errors = patch.patch(false);
        for (ContextualPatch.PatchReport report : errors) {
            // catch failed patches
            if (report.getStatus() == ContextualPatch.PatchStatus.Failure) {
                getLogger().log(LogLevel.ERROR, "Patching failed: " + report.getTarget(), report.getFailure());

                // now spit the hunks
                for (ContextualPatch.HunkReport hunk : report.getHunks()) {
                    // catch the failed hunks
                    if (!hunk.getStatus().isSuccess()) {
                        getLogger().error("Hunk %d failed!", hunk.getIndex());
                    }
                }

                throw report.getFailure();
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
            }else {
                getLogger().log(LogLevel.ERROR, "Patch succeeded: " + report.getTarget());
            }
        }

        if (fuzzed)
            getLogger().lifecycle("Patches Fuzzed!");
    }

    private void applyMcpCleanup(File conf) throws IOException {
        ASFormatter formatter = new ASFormatter();
        OptParser parser = new OptParser(formatter);
        parser.parseOptionFile(conf);

        Reader reader;
        Writer writer;

        HashMap<String, String> newSources = new HashMap<String, String>();

        for (String file : sourceMap.keySet()) {
            getLogger().info("Loading text");
            String text = sourceMap.get(file);
            getLogger().info("Loaded");

            getLogger().info("Processing file: " + file);

            getLogger().info("processing comments");
            text = McpCleanup.stripComments(text);

            getLogger().info("fixing imports comments");
            text = McpCleanup.fixImports(text);

            getLogger().info("various other cleanup");
            text = McpCleanup.cleanup(text);

            getLogger().info("formatting source");
            reader = new StringReader(text);
            writer = new StringWriter();
            formatter.format(reader, writer);
            reader.close();
            writer.flush();
            writer.close();
            text = writer.toString();
            newSources.put(file.split("minecraft_server", 2)[1], text);
        }

        sourceMap.clear();
        sourceMap = newSources;
    }

    private void saveJar(File output) throws IOException {
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(outZip));

        // write in resources
        for (Map.Entry<String, byte[]> entry : resourceMap.entrySet()) {
            zout.putNextEntry(new ZipEntry(entry.getKey()));
            zout.write(entry.getValue());
            zout.closeEntry();
        }

        // write in sources
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            zout.putNextEntry(new ZipEntry(entry.getKey()));
            zout.write(entry.getValue().getBytes());
            zout.closeEntry();
        }

        zout.close();
    }

    /**
     * A private inner class to be used with the MCPPatches only.
     */
    private static class ContextProvider implements ContextualPatch.IContextProvider {
        private Map<String, String> fileMap;

        private final int STRIP = 0;

        public ContextProvider(Map<String, String> fileMap) {
            this.fileMap = fileMap;
        }

        private String strip(String target) {
            target = target.replace('\\', '/');
            int index = 0;
            for (int x = 0; x < STRIP; x++) {
                index = target.indexOf('/', index) + 1;
            }
            return target.substring(index);
        }

        @Override
        public List<String> getData(String target) {
            target = strip(target);

            if (fileMap.containsKey(target)) {
                String[] lines = fileMap.get(target).split("\r\n|\r|\n");
                List<String> ret = new ArrayList<String>();
                for (String line : lines) {
                    ret.add(line);
                }
                return ret;
            }

            return null;
        }

        @Override
        public void setData(String target, List<String> data) {
            fileMap.put(strip(target), Joiner.on(Constants.NEWLINE).join(data));
        }
    }
}
