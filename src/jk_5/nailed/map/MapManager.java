package jk_5.nailed.map;

import jk_5.nailed.Nailed;
import jk_5.nailed.config.helper.ConfigFile;
import jk_5.nailed.map.gameloop.GameThread;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Class that manages the mappacks and reads them
 *
 * @author jk-5
 */
public class MapManager {

    public final File mapPack = new File(Nailed.config.getTag("mappack").getTag("path").setComment("Path to the mappack file").getValue("mappack.zip"));

    //private Properties config;
    private ConfigFile config;

    private final GameThread gameThread = new GameThread();

    public void readMapConfig() {
        ZipInputStream stream = null;
        boolean foundConfig = false;
        boolean foundGameInstructions = false;
        try {
            stream = new ZipInputStream(new FileInputStream(this.mapPack));
            ZipEntry entry;
            while ((entry = stream.getNextEntry()) != null) {
                if (entry.getName().equals("mappack.cfg")) {
                    foundConfig = true;
                    this.config = new ConfigFile(null, new InputStreamReader(stream)).setReadOnly(true);
                } else if (entry.getName().equals("gameinstructions.cfg")) {
                    foundGameInstructions = true;
                    this.gameThread.parseInstructions(stream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            try {
                if (stream != null) stream.close();
            } catch (IOException e) {
                //Wow
            }
        }
        if (!foundConfig) {
            System.err.println("Was not able to read the mappack.cfg file from the mappack file");
            System.exit(1);
        }
        if (!foundGameInstructions) {
            System.err.println("Was not able to read the gameinstructions.cfg file from the mappack file");
            System.exit(1);
        }
    }

    public GameThread getGameThread() {
        return this.gameThread;
    }

    public ConfigFile getConfig() {
        return this.config;
    }
}
