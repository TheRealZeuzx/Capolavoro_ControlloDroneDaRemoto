import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SendCommand implements Command{
    private DatagramSocket socket;
    private DatagramPacket packet;
    public SendCommand(DatagramSocket socket,DatagramPacket packet){
        this.socket = socket;
        this.packet = packet;
    }

    public void execute(){
        try {
            this.socket.send(this.packet);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //?aggiungi comando per stampare eccezioni sia a video che su file.
    }

}
