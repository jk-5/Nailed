package jk_5.nailed.buildscript;

import com.google.common.base.Joiner;
import org.gradle.api.Project;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final String EXT_NAME = "minecraft";

    //Url's
    public static String MC_VERSION(Project project){
        return ((ExtensionObject) project.getExtensions().getByName(EXT_NAME)).getMinecraftVersion();
    }

    public static String withVersion(Project project, String format){
        return format.replaceAll("%%version%%", MC_VERSION(project));
    }

    public static final String MC_SERVER_URL = "http://s3.amazonaws.com/Minecraft.Download/versions/%%version%%/minecraft_server.%%version%%.jar";
    public static final String FERNFLOWER_URL = "https://github.com/nailed/nailed/raw/buildscript/mcplibs/fernflower.jar";
    public static final String EXCEPTOR_URL = "https://github.com/nailed/nailed/raw/buildscript/mcplibs/mcinjector.jar";

    //Cached files
    public static final String CACHE_DIR = "caches/nailed";
    public static final String JAR_SERVER_FRESH = CACHE_DIR + "/net/minecraft/minecraft_server/%%version%%/minecraft_server-%%version%%.jar";
    public static final String JAR_SRG = CACHE_DIR + "/net/minecraft/minecraft_srg/%%version%%/minecraft_srg-%%version%%.jar";
    public static final String ZIP_DECOMP = CACHE_DIR + "/net/minecraft/minecraft_decomp/%%version%%/minecraft_decomp-%%version%%.zip";
    public static final String PACKAGED_SRG = CACHE_DIR + "/net/minecraft/minecraft_srg/%%version%%/packaged-%%version%%.srg";
    public static final String PACKAGED_EXC = CACHE_DIR + "/net/minecraft/minecraft_srg/%%version%%/packaged-%%version%%.exc";
    public static final String FERNFLOWER = "caches/fernflower.jar";
    public static final String EXCEPTOR = "caches/exceptor.jar";

    //Src directories
    public static final String MINECRAFT_CLEAN_DIR = "minecraft/%%version%%/clean";
    public static final String MINECRAFT_WORK_DIR = "minecraft/%%version%%/work";
    public static final String MINECRAFT_RESOURCES_DIR = "minecraft/%%version%%/resources";
    public static final String NAILED_SRC = "src/main/scala";
    public static final String NAILED_RES = "src/main/resources";
    public static final String PATCH_DIR = "patches";

    //Mappings
    public static final String MAPPINGS_DIR = "conf/%%version%%";
    public static final String METHOD_CSV = MAPPINGS_DIR + "/methods.csv";
    public static final String FIELDS_CSV = MAPPINGS_DIR + "/fields.csv";
    public static final String PARAMS_CSV = MAPPINGS_DIR + "/params.csv";
    public static final String PACK_CSV = MAPPINGS_DIR + "/packages.csv";
    public static final String JOINED_SRG = MAPPINGS_DIR + "/joined.srg";
    public static final String JOINED_EXC = MAPPINGS_DIR + "/joined.exc";
    public static final String ASTYLE_CFG = MAPPINGS_DIR + "/astyle.cfg";

    // various useful files
    public static final String MCP_PATCH = MAPPINGS_DIR + "/patches/minecraft_server_ff.patch";
    public static final String PACKAGED_PATCH = Constants.CACHE_DIR + "/net/minecraft/minecraft_srg/%%version%%/packaged-%%version%%.patch";

    // helper methods
    public static File projectFile(Project project, String... otherFiles) {
        return Constants.file(project.getProjectDir(), otherFiles);
    }

    // util
    public static final String NEWLINE = System.getProperty("line.separator");

    // helper methods
    public static File cacheFile(Project project, String... otherFiles) {
        return Constants.file(project.getGradle().getGradleUserHomeDir(), otherFiles);
    }

    public static File file(File file, String... otherFiles) {
        String othersJoined = Joiner.on('/').join(otherFiles);
        return new File(file, othersJoined);
    }

    public static File file(String... otherFiles) {
        String othersJoined = Joiner.on('/').join(otherFiles);
        return new File(othersJoined);
    }

    public static List<String> getClassPath() {
        URL[] urls = ((URLClassLoader) ExtensionObject.class.getClassLoader()).getURLs();

        ArrayList<String> list = new ArrayList<String>();
        for (URL url : urls) {
            list.add(url.getPath());
        }
        //System.out.println(Joiner.on(';').join(((URLClassLoader) ExtensionObject.class.getClassLoader()).getURLs()));
        return list;
    }

    public static String hash(File file) {
        try {

            InputStream fis = new FileInputStream(file);

            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead;

            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            fis.close();
            byte[] hash = complete.digest();

            String result = "";

            for (byte h : hash) {
                result += Integer.toString((h & 0xff) + 0x100, 16).substring(1);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String hash(String str) {
        try {
            MessageDigest complete = MessageDigest.getInstance("MD5");
            byte[] hash = complete.digest(str.getBytes());

            String result = "";
            for (byte h : hash){
                result += Integer.toString((h & 0xff) + 0x100, 16).substring(1);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * DON'T FORGET TO CLOSE
     */
    public static OutputStream getNullStream() {
        return new BufferedOutputStream(new ByteArrayOutputStream());
    }
}
