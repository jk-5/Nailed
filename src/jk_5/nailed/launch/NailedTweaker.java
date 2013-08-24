package jk_5.nailed.launch;

import com.google.common.base.Throwables;
import com.google.common.collect.ObjectArrays;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class NailedTweaker implements ITweaker {

    private List<String> args;
    private File gameDir;
    private File assetsDir;
    private String profile;
    private static URI jarLocation;

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile){
        this.gameDir = (gameDir == null ? new File(".") : gameDir);
        this.assetsDir = assetsDir;
        this.profile = profile;
        this.args = args;
        try{
            jarLocation = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
        }catch (URISyntaxException e){
            System.err.println("Missing URI information for Nailed tweak");
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader){
        classLoader.addTransformerExclusion("jk_5.nailed.asm.transformers");
        classLoader.registerTransformer("jk_5.nailed.asm.transformers.AccessTransformer");
    }

    @Override
    public String getLaunchTarget(){
        return "net.minecraft.server.MinecraftServer";
    }

    @Override
    public String[] getLaunchArguments(){
        String[] array = args.toArray(new String[args.size()]);
        if (gameDir != null && !Arrays.asList(array).contains("--gameDir")){
            array = ObjectArrays.concat(gameDir.getAbsolutePath(),array);
            array = ObjectArrays.concat("--gameDir",array);
        }

        if (assetsDir != null && !Arrays.asList(array).contains("--assetsDir")){
            array = ObjectArrays.concat(assetsDir.getAbsolutePath(), array);
            array = ObjectArrays.concat("--assetsDir",array);
        }
        if (profile != null && !Arrays.asList(array).contains("--version")){
            array = ObjectArrays.concat(profile,array);
            array = ObjectArrays.concat("--version",array);
        }else if (!Arrays.asList(array).contains("--version")){
            array = ObjectArrays.concat("UnknownNailedProfile",array);
            array = ObjectArrays.concat("--version",array);
        }
        return array;
    }
}
