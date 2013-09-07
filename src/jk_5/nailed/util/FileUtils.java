package jk_5.nailed.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * No description given
 *
 * @author jk-5
 */
public class FileUtils {

    public static void unzip(File input, File outputDir) {
        try{
            if(!outputDir.exists()) outputDir.mkdir();

            ZipFile zipFile = new ZipFile(input);
            Enumeration e = zipFile.entries();

            while(e.hasMoreElements()){
                ZipEntry entry = (ZipEntry)e.nextElement();
                File destinationFilePath = new File(outputDir,entry.getName());
                destinationFilePath.getParentFile().mkdirs();
                if(!entry.isDirectory()){
                    BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

                    int b;
                    byte buffer[] = new byte[1024];
                    FileOutputStream fos = new FileOutputStream(destinationFilePath);
                    BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);
                    while ((b = bis.read(buffer, 0, 1024)) != -1) {
                        bos.write(buffer, 0, b);
                    }
                    bos.flush();
                    bos.close();
                    bis.close();
                }
            }
        }catch(IOException ioe){

        }
    }

    public static File unzipMapFromMapPack(File mapPack, File destDir){
        try{
            ZipFile zipFile = new ZipFile(mapPack);
            Enumeration e = zipFile.entries();
            File worldDir = null;
            while(e.hasMoreElements()){
                ZipEntry entry = (ZipEntry)e.nextElement();
                if(entry.getName().equals("mappack.cfg")) continue;
                if(entry.getName().equals("gameinstructions.cfg")) continue;
                if(entry.getName().contains("##MCEDIT.TEMP##")) continue;
                if(entry.getName().startsWith("__MACOSX/")) continue;
                File destinationFilePath = new File(destDir.getParentFile(), entry.getName());
                destinationFilePath.getParentFile().mkdirs();
                if(!entry.isDirectory()){
                    BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

                    int b;
                    byte buffer[] = new byte[1024];
                    FileOutputStream fos = new FileOutputStream(destinationFilePath);
                    BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);
                    while ((b = bis.read(buffer, 0, 1024)) != -1) {
                        bos.write(buffer, 0, b);
                    }
                    bos.flush();
                    bos.close();
                    bis.close();
                }else if(entry.getName().equals("world/")){
                    worldDir = destinationFilePath;
                }
            }
            if(worldDir == null){
                System.err.println("Invalid or corrupt mappack file");
                System.exit(1);
            }
            worldDir.renameTo(destDir);
            return destDir;
        }catch(IOException ioe){

        }
        return null;
    }
}
