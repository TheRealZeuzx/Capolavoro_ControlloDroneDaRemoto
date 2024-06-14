package it.davincifascetti.controllosocketudp.program;

import java.util.concurrent.atomic.AtomicBoolean;

import it.davincifascetti.controllosocketudp.command.CommandException;
import me.friwi.tello4j.api.drone.TelloDrone;
import me.friwi.tello4j.api.drone.WifiDroneFactory;
import me.friwi.tello4j.api.exception.TelloException;
import me.friwi.tello4j.api.exception.TelloNetworkException;
import me.friwi.tello4j.api.video.VideoWindow;
import me.friwi.tello4j.wifi.impl.video.TelloVideoThread;

public class Video extends Component implements Runnable {
    public static final int TIME_BEFORE_CLOSING_FRAME = 2000;
    private final static AtomicBoolean INIT = new AtomicBoolean();
    private VideoWindow videoWindow = null;
    private Thread thread = null;
    private TelloVideoThread tv = null;

    public Video(Ui ui) throws CommandException{
        this.setUi(ui);
        this.videoWindow = new VideoWindow("FPV drone");
        try {
            tv =  new TelloVideoThread(videoWindow);
        } catch (TelloNetworkException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void start(){
        if(Video.INIT.compareAndSet(false, true)){
           this.thread = new Thread(this);
           this.thread.start();
        }
    }

    public void run(){
        try{
            this.tv.start();
        }catch(Exception e){
            Video.INIT.set(false);
            ((Terminal) this.getUi()).getCli().printError(e.getMessage());
        }
    }

    public void destroy(){
        this.tv.finish();
        super.destroy();

    }

    public void updateVideo(byte[] buffer,int lung){
        try {
            this.tv.handleInput(buffer, lung);
        } catch (TelloException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
