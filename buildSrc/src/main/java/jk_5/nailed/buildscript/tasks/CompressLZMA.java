package jk_5.nailed.buildscript.tasks;

import lombok.Getter;
import lombok.Setter;
import lzma.sdk.lzma.Decoder;
import lzma.streams.LzmaInputStream;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import java.io.*;

public class CompressLZMA extends DefaultTask {
    @Getter
    @Setter
    @InputFile
    private File inputFile;

    @Getter
    @Setter
    @OutputFile
    private File outputFile;

    @TaskAction
    public void doTask() throws IOException {
        final LzmaInputStream in = new LzmaInputStream(new BufferedInputStream(new FileInputStream(outputFile)), new Decoder());
        final OutputStream out = new BufferedOutputStream(new FileOutputStream(inputFile));

        int num = in.read();
        while (num != -1) {
            out.write(num);
            num = in.read();
        }

        in.close();
        out.close();
    }
}
