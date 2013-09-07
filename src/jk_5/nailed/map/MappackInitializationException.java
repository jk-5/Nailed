package jk_5.nailed.map;

/**
 * No description given
 *
 * @author jk-5
 */
public class MappackInitializationException extends RuntimeException {

    public MappackInitializationException(Mappack pack){
        super("Error while reading mappack " + pack.getName());
    }

    public MappackInitializationException(Mappack pack, Throwable t){
        super("Error while reading mappack " + pack.getName(), t);
    }

    public MappackInitializationException(Mappack pack, String msg){
        super("Error while reading mappack " + pack.getName() + ": " + msg);
    }

    public MappackInitializationException(Mappack pack, String msg, Throwable t){
        super("Error while reading mappack " + pack.getName() + ": " + msg, t);
    }
}
