package jk_5.nailed.util;

/**
 * No description given
 *
 * @author jk-5
 */
@Deprecated
public enum EnumColor {
    BLACK("\u00a70"),
    DARKBLUE("\u00a71"),
    DARKGREEN("\u00a72"),
    DARKAQUA("\u00a73"),
    DARKRED("\u00a74"),
    PURPLE("\u00a75"),
    GOLD("\u00a76"),
    GREY("\u00a77"),
    DARKGREY("\u00a78"),
    INDIGO("\u00a79"),
    GREEN("\u00a7a"),
    AQUA("\u00a7b"),
    RED("\u00a7c"),
    PINK("\u00a7d"),
    YELLOW("\u00a7e"),
    WHITE("\u00a7f"),
    RANDOM("\u00a7k"),
    BOLD("\u00a7l"),
    CODE("\u00a7"),
    STRIKE("\u00a7m"),
    UNDERLINE("\u00a7n"),
    ITALICS("\u00a7o"),
    RESET("\u00a7r");

    private final String color;

    EnumColor(String color){
        this.color = color;
    }

    public String toString(){
        return this.color;
    }
}
