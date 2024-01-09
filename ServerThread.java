import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class ServerThread extends Thread{
    private static final int LunghezzaBuffer = 1024;
    DatagramPacket packet;
    DatagramSocket socketRisposta;

    //TODO creare i seguenti metodi per eseguire le op di :
    //? TODO risposta al client con stesso msg?
    //TODO salva su file msg client

    //TODO Cambiare da Exception a un eccezione apposita
    public ServerThread(DatagramPacket packet, DatagramSocket socketRisposta){
        this.packet = packet;
        this.socketRisposta = socketRisposta;
    }

    @Override
    public void run() {
        //estraggo il contenuto testuale
		int lungPacket = this.packet.getLength();
		String msgRicevuto = new String(this.packet.getData());
		msgRicevuto = msgRicevuto.substring(0, lungPacket);

		byte[] bufferOUT = new byte[ServerThread.LunghezzaBuffer];
		System.out.println("msg ricevuto: " + msgRicevuto);
		

		String daSpedire = msgRicevuto.toUpperCase();
		bufferOUT = daSpedire.getBytes();
        InetAddress ClientIP = this.packet.getAddress();
        int ClientPort = this.packet.getPort();
		DatagramPacket packetDaSpedire = new DatagramPacket(bufferOUT, bufferOUT.length,ClientIP,ClientPort);

		try {
			this.socketRisposta.send(packetDaSpedire);
			System.out.println("risposta: " + daSpedire);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		
    }
}



