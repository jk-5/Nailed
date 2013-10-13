package jk_5.nailed.buildscript.tasks;

import lombok.Getter;
import lombok.Setter;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends CachedTask {
    @Setter
    @Getter
    @Input
    private String url;

    @Getter
    @Setter
    @OutputFile
    @Cached
    private File output;

    @TaskAction
    public void doTask() throws IOException {
        File outputFile = getProject().file(output);
        outputFile.getParentFile().mkdirs();
        outputFile.createNewFile();

        getLogger().info("Downloading " + url + " to " + outputFile);

        HttpURLConnection connect = (HttpURLConnection) (new URL(url)).openConnection();
        connect.setInstanceFollowRedirects(true);

        InputStream inStream = connect.getInputStream();
        OutputStream outStream = new FileOutputStream(outputFile);

        int data = inStream.read();
        while (data != -1) {
            outStream.write(data);

            // read next
            data = inStream.read();
        }

        inStream.close();
        outStream.flush();
        outStream.close();

        getLogger().info("Download complete");
    }
}
