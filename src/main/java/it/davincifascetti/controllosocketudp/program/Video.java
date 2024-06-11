package it.davincifascetti.controllosocketudp.program;

import java.util.concurrent.atomic.AtomicBoolean;

import it.davincifascetti.controllosocketudp.command.CommandException;
import me.friwi.tello4j.api.drone.TelloDrone;
import me.friwi.tello4j.api.drone.WifiDroneFactory;
import me.friwi.tello4j.api.video.VideoWindow;

public class Video extends Component {
    public static final int TIME_BEFORE_CLOSING_FRAME = 2000;
    private final static AtomicBoolean INIT = new AtomicBoolean();
    private TelloDrone listeningTo = null;
    private VideoWindow videoWindow = null;
    private boolean alive = false;

    public static void main(String[] args) throws Exception {

    }

    public Video(Ui ui) throws CommandException{
        this.setUi(ui);
    }

    public void start(){
        if(Video.INIT.compareAndSet(false, true)){
            try (TelloDrone drone = new WifiDroneFactory().build()) {
                this.videoWindow = new VideoWindow();
                drone.connect();
                drone.addVideoListener(this.videoWindow);
                drone.setStreaming(true);
                this.alive = true;
                while (this.alive) ;
            }catch(Exception e){
                Video.INIT.set(false);
                ((Terminal) this.getUi()).getCli().printError(e.getMessage());
            }
        }
    }

    public void destroy(){
        this.alive = false;
        super.destroy();
        this.listeningTo.removeVideoListener(this.videoWindow);
        this.videoWindow.dispose();
        this.listeningTo = null;
    }
}
