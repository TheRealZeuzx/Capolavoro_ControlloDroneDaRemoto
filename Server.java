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
    private DatagramSocket socket;
    private String nomeServer;

    public Server(DatagramSocket socket,String nomeServer) throws Exception{
        if(!this.setNomeServer(nomeServer)) throw new Exception("il nome inserito non è valido");
    }

    @Override
    public void run() {
    }

    public void main(){
        
    }

    
 
    @Override
    public String toString() {
        
        return this.nomeServer + "\t"+ this.socket.getInetAddress() +"\t"+ this.socket.getPort();
    }

    private boolean setNomeServer(String nome){
        //è valido solo se non ci sono spazi ed è possibile usare solo lettere e _ (deve esserci almeno una lettera e almeno 2 a 18 caratteri)
        if(nome.matches("^(?=.*[a-zA-Z])[a-zA-Z_]{2,18}$")){
            this.nomeServer = nome;
            return true;
        }
        return false;
    }

}
