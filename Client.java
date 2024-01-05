
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client implements Runnable{

	private static final int LunghezzaBuffer = 1024;
    private DatagramPacket packet;
    private DatagramSocket socket;
    private String nomeClient;

    public Client(DatagramSocket socket,String nomeClient) throws Exception{
        if(!this.setnomeClient(nomeClient)) throw new Exception("il nome inserito non è valido");
    }

    @Override
    public void run() {
    }

    public void main(){
        
    }

    
 
    @Override
    public String toString() {
        
        return this.nomeClient + "\t"+ this.socket.getInetAddress() +"\t"+ this.socket.getPort();
    }

    private boolean setnomeClient(String nome){
        //è valido solo se non ci sono spazi ed è possibile usare solo lettere e _ (deve esserci almeno una lettera e almeno 2 a 18 caratteri)
        if(nome.matches("^(?=.*[a-zA-Z])[a-zA-Z_]{2,18}$")){
            this.nomeClient = nome;
            return true;
        }
        return false;
    }

}

