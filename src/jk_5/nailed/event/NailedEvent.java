package jk_5.nailed.event;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class NailedEvent {

    private boolean canceled = false;

    public boolean isCancelable(){
        return false;
    }

    public void setCanceled(Boolean cancel){
        this.canceled = cancel;
    }

    public boolean isCanceled(){
        return this.isCancelable() && this.canceled;
    }
}
