package jk_5.nailed.buildscript.tasks;

import com.google.common.io.Files;
import lombok.Getter;
import lombok.Setter;
import jk_5.nailed.buildscript.sourcemanip.NailedCleanup;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class NailedCleanupTask extends DefaultTask {
    @Getter
    @Setter
    @InputDirectory
    @OutputDirectory
    private File dir;

    private static final Pattern BEFORE = Pattern.compile("(?m)((case|default).+(?:\\r\\n|\\r|\\n))(?:\\r\\n|\\r|\\n)"); // Fixes newline after case before case body
    private static final Pattern AFTER = Pattern.compile("(?m)(?:\\r\\n|\\r|\\n)((?:\\r\\n|\\r|\\n)[ \\t]+(case|default))"); // Fixes newline after case body before new case

    @TaskAction
    public void doStuff() throws IOException {
        processDir(dir);
    }

    private void processDir(File dir) throws IOException {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                processDir(file);
            } else if (file.getPath().endsWith(".java")) {
                processFile(file);
            }
        }
    }

    private void processFile(File file) throws IOException {
        getLogger().debug("Processing file: " + file);
        String text = Files.toString(file, Charset.defaultCharset());

        getLogger().debug("preprocessing astyle case glitch");
        text = BEFORE.matcher(text).replaceAll("$1");
        text = AFTER.matcher(text).replaceAll("$1");

        getLogger().debug("Applying Nailed fixes");
        text = NailedCleanup.renameClass(text);

        getLogger().debug("Writing file");
        Files.write(text, file, Charset.defaultCharset());
    }
}
