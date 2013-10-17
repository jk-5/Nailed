package jk_5.nailed.buildscript.tasks;

import com.google.common.io.Files;
import jk_5.jsonlibrary.JsonObject;
import jk_5.jsonlibrary.JsonValue;
import jk_5.jsonlibrary.ParserException;
import jk_5.nailed.buildscript.Constants;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import scala.collection.JavaConversions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ProjectTask extends DefaultTask {

    private final ArrayList<String> deps = new ArrayList<String>();

    @InputFile
    private final File jsonFile = Constants.projectFile(getProject(), "jsons/" + Constants.MC_VERSION + ".json");
    @OutputFile
    private final File gradleFile = Constants.projectFile(getProject(), "build.gradle");

    @TaskAction
    @SuppressWarnings("unused")
    public void doTask() throws IOException, ParserException {
        parseJson();
        writeFile();
    }

    private void parseJson() throws IOException, ParserException {
        JsonObject node = JsonObject.readFrom(Files.newReader(this.jsonFile, Charset.defaultCharset()));
        for (JsonValue lib : JavaConversions.asJavaCollection(node.get("libraries").asArray().getValues())) {
            JsonObject library = lib.asObject();
            if (!(library.get("name").asString().contains("fixed") || library.get("natives") != null || library.get("extract") != null)) {
                deps.add(library.get("name").asString());
            }
        }
    }

    private void writeFile() throws IOException {
        this.gradleFile.getParentFile().mkdirs();
        Files.touch(this.gradleFile);

        StringBuilder out = new StringBuilder();

        out.append("apply plugin: 'nailed' ").append(Constants.NEWLINE);
        out.append("apply plugin: 'java' ").append(Constants.NEWLINE);
        out.append("apply plugin: 'scala' ").append(Constants.NEWLINE).append(Constants.NEWLINE);

        out.append("repositories {").append(Constants.NEWLINE);
        out.append("    maven {").append(Constants.NEWLINE);
        out.append("        name 'jk-5'").append(Constants.NEWLINE);
        out.append("        url 'https://dl.dropboxusercontent.com/u/224513697/maven/'").append(Constants.NEWLINE);
        out.append("    }").append(Constants.NEWLINE);
        out.append("    maven {").append(Constants.NEWLINE);
        out.append("        name 'minecraft'").append(Constants.NEWLINE);
        out.append("        url 'http://s3.amazonaws.com/Minecraft.Download/libraries'").append(Constants.NEWLINE);
        out.append("    }").append(Constants.NEWLINE);
        out.append("    mavenCentral()").append(Constants.NEWLINE);
        out.append("}").append(Constants.NEWLINE).append(Constants.NEWLINE);

        out.append("dependencies {").append(Constants.NEWLINE);
        for (String dep : deps) {
            out.append("    compile '").append(dep).append('\'').append(Constants.NEWLINE);
        }
        out.append(Constants.NEWLINE).append("    testCompile 'junit:junit:4.+'").append(Constants.NEWLINE);
        out.append('}').append(Constants.NEWLINE).append(Constants.NEWLINE);

        out.append("sourceSets {").append(Constants.NEWLINE);
        out.append("    main {").append(Constants.NEWLINE);
        out.append("        scala {").append(Constants.NEWLINE);
        out.append("            srcDirs 'minecraft/work', 'src/main/scala'").append(Constants.NEWLINE);
        out.append("        }").append(Constants.NEWLINE);
        out.append("        resources {").append(Constants.NEWLINE);
        out.append("            srcDirs 'minecraft/resources', 'src/main/resources'").append(Constants.NEWLINE);
        out.append("        }").append(Constants.NEWLINE);
        out.append("    }").append(Constants.NEWLINE);
        out.append('}').append(Constants.NEWLINE).append(Constants.NEWLINE);

        out.append("jar {").append(Constants.NEWLINE);
        out.append("    from configurations.compile.collect {").append(Constants.NEWLINE);
        out.append("        it.isDirectory() ? it : zipTree(it).matching {").append(Constants.NEWLINE);
        out.append("            exclude 'META-INF', 'META-INF/**', '*META-INF*', 'meta-inf'").append(Constants.NEWLINE);
        out.append("        }").append(Constants.NEWLINE);
        out.append("    }").append(Constants.NEWLINE);
        out.append("    manifest {").append(Constants.NEWLINE);
        out.append("        attributes 'Main-Class': 'jk_5.nailed.launch.ServerLauncher'").append(Constants.NEWLINE);
        out.append("    }").append(Constants.NEWLINE);
        out.append("}").append(Constants.NEWLINE);

        Files.write(out.toString(), this.gradleFile, Charset.defaultCharset());
    }
}