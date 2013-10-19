package jk_5.nailed.buildscript;

import lombok.Getter;
import lombok.Setter;
import org.gradle.api.Plugin;

public class ExtensionObject {

    @Getter
    private String minecraftVersion;

    public ExtensionObject(Plugin plugin) {
        // nothing
    }
}
