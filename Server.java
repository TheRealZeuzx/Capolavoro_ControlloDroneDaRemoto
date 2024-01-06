import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server extends SocketUDP implements Runnable{

    //? TODO risposta al client con stesso msg?
    //TODO salva su file msg client
    //TODO
    private static final int LunghezzaBuffer = 1024;
    private DatagramPacket packet;
    private DatagramSocket socket;

    public Server(DatagramSocket socket,String nomeServer) throws Exception{
        if(!super.setNome(nomeServer)) throw new Exception("il nome inserito non è valido");
    }

    @Override
    public void run() {
    }

    public void main(){
        
    }

    
 
    @Override
    public String toString() {
        
        return super.nome + "\t"+ this.socket.getInetAddress() +"\t"+ this.socket.getPort();
    }

    

}
