import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server implements Runnable,Commandable{

    //TODO creare i seguenti metodi per eseguire le op di :
    //? TODO risposta al client con stesso msg?
    //TODO salva su file msg client
    
    private static final int LunghezzaBuffer = 1024;
    //?private DatagramPacket packet;
    private DatagramSocket socket;
    private String nome;
    private boolean statoAttivo=false;
    private Thread threadAscolto = null;

    //TODO Cambiare da Exception a un eccezione apposita
    public Server(String nomeServer) throws Exception{
        if(!this.setNome(nomeServer)) throw new Exception("il nome inserito non è valido");
    }

    public Server(String nomeServer, int porta) throws Exception{
        this(nomeServer);
        this.socket = new DatagramSocket(porta);
    }
    
    @Override
    public void run(){
        
        byte[] bufferIN = new byte[Server.LunghezzaBuffer];
        byte[] bufferOUT = new byte[Server.LunghezzaBuffer];
        //TODO se è attivo && vivo si mette 
        while(this.getStato()){
            DatagramPacket pacchetto = new DatagramPacket(bufferIN, Server.LunghezzaBuffer);
            try {
                this.socket.receive(pacchetto);
                Thread threadRisposta = null;
                threadRisposta = new Thread(new ServerThread(pacchetto));
                threadRisposta.start();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } 
    }

    public void iniziaAscolto(){
        this.statoAttivo = true;
        this.threadAscolto = new Thread(this);
        threadAscolto.start();
    }

    public void terminaAscolto(){
        this.statoAttivo = false;
        this.threadAscolto.interrupt();
    }

    @Override
    public String toString() {
        return this.getNome() + "\t"+ this.socket.getInetAddress() +"\t"+ this.socket.getPort() +"\t"+ (this.getStato() ? "attivo" : "disattivo");
    }

    @Override
    public void startTerminal() {
        
    }

    public boolean getStato(){return this.statoAttivo;}

    public void setStato(boolean stato){this.statoAttivo = stato;}

    public String getNome(){return this.nome;}

    public boolean setNome(String nome){
        //è valido solo se non ci sono spazi ed è possibile usare solo lettere e _ (deve esserci almeno una lettera e almeno 2 a 18 caratteri)
        if(nome.matches("^(?=.*[a-zA-Z])[a-zA-Z_]{2,18}$")){
            this.nome = nome;
            return true;
        }
        return false;
    }

}
