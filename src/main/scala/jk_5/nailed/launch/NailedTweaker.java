package jk_5.nailed.launch;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public class NailedTweaker implements ITweaker {

    private static URI jarLocation;

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile){
        try{
            jarLocation = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
        }catch (URISyntaxException e){
            System.err.println("Missing URI information for Nailed tweak");
            throw new RuntimeException(e);
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
        return new String[]{"--version", "Nailed"};
    }
}
