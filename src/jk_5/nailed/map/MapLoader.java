package jk_5.nailed.map;

import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import jk_5.nailed.Nailed;
import net.minecraft.src.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class MapLoader {

    private static final File mappackFolder = new File("mappacks");
    private static final File mapsFolder = new File("maps");

    private Map lobby;
    private final List<Map> maps = Lists.newArrayList();                //List of loaded worlds with their proper data      -> worlds
    private final List<Mappack> mappacks = Lists.newArrayList();        //Loaded mappacks, no fancy data
    private final List<WorldServer> handles = Lists.newArrayList();     //List of loaded worlds                             -> console.worlds

    public MapLoader(){
        if(!mappackFolder.exists()) mappackFolder.mkdirs();
        MapMaker maker = new MapMaker();
        maker.weakKeys();
    }

    public void loadMaps(){
        this.mappacks.clear();
        for(File file : mappackFolder.listFiles(new FilenameFilter(){
            public boolean accept(File file, String name){
                return name.endsWith(".mappack");
            }
        })){
            Mappack pack = new Mappack(file);
            this.mappacks.add(pack);
            pack.readConfig();
        }
    }

    private File worldFolderForMap(Map map){
        return new File(mapsFolder, "map" + map.getUID() + map.getMappack().getName());
    }

    public Map getMap(int uid){
        for(Map map : this.maps){
            if(map.getUID() == uid){
                return map;
            }
        }
        return null;
    }

    public Mappack getMappackFromUID(int uid){
        for(Mappack map : this.mappacks){
            if(map.getUID() == uid){
                return map;
            }
        }
        return null;
    }

    //CraftServer -> createWorld
    public Map createWorld(Mappack mappack){
        Map map = new Map(mappack);
        System.out.println(map.getUID());
        System.out.println(map.getMappack());
        System.out.println(map.getMappack().getName());
        File folder = this.worldFolderForMap(map);
        mappack.unpackMappack(folder);
        if(this.getMap(map.getUID()) != null) return map;
        if ((folder.exists()) && (!folder.isDirectory())) {
            throw new IllegalArgumentException("File exists with the name '" + folder.getName() + "' and isn't a folder");
        }
        AnvilSaveConverter converter = new AnvilSaveConverter(mapsFolder);
        if(converter.isOldMapFormat("map" + map.getFolderName())){
            System.out.println("Converting world '" + map.getFolderName() + "'");
            converter.convertMapFormat(map.getFolderName(), new ConvertingProgressUpdate(Nailed.server));
        }
        WorldServer worldServer = new WorldServer(Nailed.server, new AnvilSaveHandler(mapsFolder, map.getFolderName(), true), map.getFolderName(), map.getUID(), new WorldSettings(1L, mappack.getDefaultGamemode(), false, false, null), Nailed.server.theProfiler, Nailed.server.getLogAgent());
        worldServer.setScoreboard(new Scoreboard());
        worldServer.difficultySetting = mappack.getDifficulty();
        worldServer.setAllowedSpawnTypes(mappack.shouldSpawnHostileMobs(), mappack.shouldSpawnFriendlyMobs());
        this.handles.add(worldServer);
        System.out.println("Preparing start region for level '" + map.getFolderName() + "'");

        if (true) { //TODO: this will keep spawn in memory
            short short1 = 196;
            long i = System.currentTimeMillis();
            for (int j = -short1; j <= short1; j += 16) {
                for (int k = -short1; k <= short1; k += 16) {
                    long l = System.currentTimeMillis();

                    if (l < i) {
                        i = l;
                    }

                    if (l > i + 1000L) {
                        int i1 = (short1 * 2 + 1) * (short1 * 2 + 1);
                        int j1 = (j + short1) * (short1 * 2 + 1) + k + 1;

                        System.out.println("Preparing spawn area for " + map.getFolderName() + ", " + (j1 * 100 / i1) + "%");
                        i = l;
                    }

                    ChunkCoordinates chunkcoordinates = mappack.getSpawnPoint();
                    worldServer.getWorldChunkManager().getBiomeGenAt(chunkcoordinates.posX + j >> 4, chunkcoordinates.posZ + k >> 4);
                }
            }
        }
        this.maps.add(map);
        map.setWorldServer(worldServer);
        return map;
    }

    //CraftServer -> unloadWorld
    public boolean unloadWorld(Map map, boolean save){
        if (map == null) return false;

        WorldServer handle = map.getHandle();

        if (!this.handles.contains(handle)) return false;

        if (handle.playerEntities.size() > 0) return false;

        if (save) {
            try {
                handle.saveAllChunks(true, null);
                handle.saveLevel();
            } catch (MinecraftException ex) {
                ex.printStackTrace();
            }
        }

        maps.remove(map);
        handles.remove(handle);

        return true;
    }

    public Mappack getMappack(String name){
        for(Mappack m : this.mappacks){
            if(m.getName().equals(name)){
                return m;
            }
        }
        return null;
    }

    public void setupLobby() {
        Mappack lobby = this.getMappack("lobby");
        this.lobby = new Map(lobby);
        this.maps.add(this.lobby);
        lobby.unpackMappack(this.worldFolderForMap(this.lobby));
        Nailed.server.setFolderName("maps/" + this.lobby.getFolderName());
    }

    public void setupMapSettings(){
        this.lobby.setWorldServer(Nailed.server.worldServerForDimension(0));
        for(Mappack pack : mappacks){
            pack.setupSettings();
        }
    }

    public Map getMapFromWorld(WorldServer world){
        for(Map m : this.maps){
            if(m.getHandle() == world){
                return m;
            }
        }
        return null;
    }

    public Map getLobby(){
        return this.lobby;
    }
}
