import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class ServerThread extends Thread{

    DatagramPacket packet;

    //TODO creare i seguenti metodi per eseguire le op di :
    //? TODO risposta al client con stesso msg?
    //TODO salva su file msg client

    //TODO Cambiare da Exception a un eccezione apposita
    public ServerThread(DatagramPacket packet){
        this.packet = packet;
    }

    @Override
    public void run() {
        // risoluzione pacchetto
    }



}
