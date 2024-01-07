
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client extends SocketUDP implements Runnable{

	private static final int LunghezzaBuffer = 1024;
    private DatagramPacket packet;
    private DatagramSocket socket;
    private String nomeClient;

    public Client(DatagramSocket socket,String nomeClient) throws Exception{
        if(!this.setNome(nomeClient)) throw new Exception("il nome inserito non è valido");
    }

    @Override
    public void run() {
    }

    public void main(){
        
    }

    @Override
    public String toString() {
        
        return this.getNome() + "\t"+ this.socket.getInetAddress() +"\t"+ this.socket.getPort()+"\t"+ (this.getStato() ? "attivo" : "disattivo");
    }
}

