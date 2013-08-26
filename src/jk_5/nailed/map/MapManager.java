package jk_5.nailed.map;

import jk_5.nailed.Nailed;
import jk_5.nailed.map.gamestart.GameStartupThread;
import jk_5.nailed.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class MapManager {

    private File mapPack = new File(Nailed.config.getTag("mappack").getTag("path").setComment("Path to the mappack file").getValue("mappack.zip"));
    private File mapsFolder = new File("maps");
    private Properties config;
    private int mapId = 0;

    private GameStartupThread thread;

    public void readMapConfig() {
        ZipInputStream stream = null;
        boolean foundConfig = false;
        try {
            stream = new ZipInputStream(new FileInputStream(this.mapPack));
            ZipEntry entry;
            while ((entry = stream.getNextEntry()) != null) {
                if (entry.getName().equals("mappack.cfg")) {
                    foundConfig = true;
                    this.config = new Properties();
                    this.config.load(stream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            try {
                if (stream != null) stream.close();
            } catch (IOException e) {
            }
        }
        if (!foundConfig) {
            System.err.println("Was not able to read the mappack.cfg file from the mappack file");
            System.exit(1);
        }
    }

    public void setupSpawnMap() {
        this.thread = new GameStartupThread();
        File map = this.unpackMap();
        //Nailed.server.setFolderName("testmap");
        Nailed.server.setFolderName("maps" + System.getProperty("file.seperator", "/") + map.getName());
    }

    public GameStartupThread getThread() {
        return this.thread;
    }

    public File unpackMap() {
        return this.unpackMap(mapId++);
    }

    public File unpackMap(int id) {
        File f = new File(this.mapsFolder, "map" + id);
        if (f.exists()) return f;
        else return FileUtils.unzipMapFromMapPack(this.mapPack, this.mapsFolder, "map" + id);
    }

    public Properties getConfig() {
        return this.config;
    }
}
