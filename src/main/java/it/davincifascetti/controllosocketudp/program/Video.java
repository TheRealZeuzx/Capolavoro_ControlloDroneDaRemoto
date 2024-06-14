package it.davincifascetti.controllosocketudp.program;

import java.util.concurrent.atomic.AtomicBoolean;

import it.davincifascetti.controllosocketudp.command.CommandException;
import me.friwi.tello4j.api.drone.TelloDrone;
import me.friwi.tello4j.api.drone.WifiDroneFactory;
import me.friwi.tello4j.api.exception.TelloNetworkException;
import me.friwi.tello4j.api.video.VideoWindow;
import me.friwi.tello4j.wifi.impl.video.TelloVideoThread;

public class Video extends Component implements Runnable {
    public static final int TIME_BEFORE_CLOSING_FRAME = 2000;
    private final static AtomicBoolean INIT = new AtomicBoolean();
    private TelloDrone listeningTo = null;
    private VideoWindow videoWindow = null;
    private Thread thread = null;


    public Video(Ui ui) throws CommandException{
        this.setUi(ui);
    }

    public void start(){
        if(Video.INIT.compareAndSet(false, true)){
           this.thread = new Thread(this);
           this.thread.start();
        }
    }

    public void run(){
        try{
            this.listeningTo = new WifiDroneFactory().build();
            this.videoWindow = new VideoWindow();
            this.videoWindow.setSize(960,720);
            this.listeningTo.connect();
            this.listeningTo.addVideoListener(this.videoWindow);
            this.listeningTo.setStreaming(true);
            while (this.listeningTo.isStreaming())  ;
        }catch(Exception e){
            Video.INIT.set(false);
            ((Terminal) this.getUi()).getCli().printError(e.getMessage());
        }
    }

    public void destroy(){
        this.thread.interrupt();
        super.destroy();
        this.listeningTo.removeVideoListener(this.videoWindow);
        this.videoWindow.dispose();
        this.listeningTo = null;
    }

}
