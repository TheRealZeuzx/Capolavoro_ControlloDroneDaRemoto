import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server implements Runnable{

    //? TODO risposta al client con stesso msg?
    //TODO salva su file msg client
    //TODO
    private static final int LunghezzaBuffer = 1024;
    private DatagramPacket packet;
    private static DatagramSocket socket;

    public Server(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
    }

    public void main(){
        
    }


}
