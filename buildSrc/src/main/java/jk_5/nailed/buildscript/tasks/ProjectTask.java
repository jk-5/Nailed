package jk_5.nailed.buildscript.tasks;

import com.google.common.io.Files;
import groovy.lang.Closure;
import jk_5.jsonlibrary.JsonObject;
import jk_5.jsonlibrary.JsonValue;
import jk_5.jsonlibrary.ParserException;
import lombok.Getter;
import lombok.Setter;
import jk_5.nailed.buildscript.Constants;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import scala.collection.JavaConversions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ProjectTask extends DefaultTask {
    @Getter
    @Setter
    protected File cleanDir;

    @Getter
    @Setter
    protected File dirtyDir;

    private final ArrayList<String> deps = new ArrayList<String>();

    public ProjectTask() {
        this.getOutputs().file(getDirtyFile());
        this.getOutputs().file(getCleanFile());
    }

    @TaskAction
    public void doTask() throws IOException, ParserException {
        parseJson();
        writeFile(true);
        writeFile(false);
    }

    private void parseJson() throws IOException, ParserException {
        // parse json
        JsonObject node = JsonObject.readFrom(Files.newReader(Constants.projectFile(this.getProject(), "jsons/" + Constants.MC_VERSION + "-dev.json"), Charset.defaultCharset()));
        for (JsonValue lib : JavaConversions.asJavaCollection(node.get("libraries").asArray().getValues())) {
            JsonObject library = lib.asObject();
            if (!(library.get("name").asString().contains("fixed") || library.get("natives") != null || library.get("extract") != null)) {
                deps.add(library.get("name").asString());
            }
        }
    }

    private void writeFile(boolean nailed) throws IOException {
        File file = getProject().file(nailed ? getDirtyFile().call() : getCleanFile().call());
        file.getParentFile().mkdirs();
        Files.touch(file);

        // prepare file string for writing.
        StringBuilder out = new StringBuilder();

        // apply the java plugin
        out.append("apply plugin: 'scala' ").append(Constants.NEWLINE).append(Constants.NEWLINE);

        // add maven central and the minecraft repo
        out.append("repositories {").append(Constants.NEWLINE);
        out.append("    maven {").append(Constants.NEWLINE);
        out.append("        name 'forge'").append(Constants.NEWLINE);
        out.append("        url 'http://files.minecraftforge.net/maven'").append(Constants.NEWLINE);
        out.append("    }").append(Constants.NEWLINE);
        out.append("    maven {").append(Constants.NEWLINE);
        out.append("        name 'minecraft'").append(Constants.NEWLINE);
        out.append("        url 'http://s3.amazonaws.com/Minecraft.Download/libraries'").append(Constants.NEWLINE);
        out.append("    }").append(Constants.NEWLINE);
        out.append("    mavenCentral()").append(Constants.NEWLINE);
        out.append("}").append(Constants.NEWLINE).append(Constants.NEWLINE);

        // dependencies
        out.append("dependencies {").append(Constants.NEWLINE);
        // read json, output json in gradle freindly format...
        for (String dep : deps) {
            out.append("    compile '").append(dep).append('\'').append(Constants.NEWLINE);
        }
        // junit.
        out.append(Constants.NEWLINE).append("    testCompile 'junit:junit:4.+'").append(Constants.NEWLINE);
        out.append('}').append(Constants.NEWLINE).append(Constants.NEWLINE);

        if (nailed) {
            out.append("sourceSets {").append(Constants.NEWLINE);
            out.append("    main {").append(Constants.NEWLINE);
            out.append("        java {").append(Constants.NEWLINE);
            out.append("            srcDirs 'src/minecraft', 'src/nailed', 'src/testmod'").append(Constants.NEWLINE);
            out.append("        }").append(Constants.NEWLINE);
            out.append("        resources {").append(Constants.NEWLINE);
            out.append("            srcDirs 'src/resources', 'src/testmod'").append(Constants.NEWLINE);
            out.append("        }").append(Constants.NEWLINE).append("    }").append(Constants.NEWLINE).append('}');
        }

        Files.write(out.toString(), file, Charset.defaultCharset());
    }

    private Closure getCleanFile() {
        return new Closure(this) {
            @Override
            public Object call() {
                return new File(getCleanDir(), "build.gradle");
            }

            @Override
            public Object call(Object obj) {
                return new File(getCleanDir(), "build.gradle");
            }
        };
    }

    private Closure getDirtyFile() {
        return new Closure(this) {
            @Override
            public Object call() {
                return new File(getDirtyDir(), "build.gradle");
            }

            @Override
            public Object call(Object obj) {
                return new File(getDirtyDir(), "build.gradle");
            }
        };
    }
}