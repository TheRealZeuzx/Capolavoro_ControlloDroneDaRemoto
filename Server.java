import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server implements Runnable{
    private static final int LunghezzaBuffer = 1024;
    private DatagramPacket packet;
    private static DatagramSocket socket;
    public Server(DatagramPacket packet) {

        this.packet = packet;

    }

    @Override
    public void run() {
        try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			
		}
        //estraggo il contenuto testuale
		int lungPacket = this.packet.getLength();
		String msgRicevuto = new String(this.packet.getData());
		msgRicevuto = msgRicevuto.substring(0, lungPacket);


		byte[] bufferOUT = new byte[Server.LunghezzaBuffer];
		System.out.println("msg ricevuto: " + msgRicevuto);
		

		String daSpedire = msgRicevuto.toUpperCase();
		bufferOUT = daSpedire.getBytes();
        InetAddress ClientIP = this.packet.getAddress();
        int ClientPort = this.packet.getPort();
		DatagramPacket packetDaSpedire = new DatagramPacket(bufferOUT, bufferOUT.length,ClientIP,ClientPort);

		try {
			Server.socket.send(packetDaSpedire);
			System.out.println("risposta: " + daSpedire);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		
    }

    //! MAIN ----
    public static void main(String[] args) throws Exception{
        Server.socket = new DatagramSocket(8890);
        byte[] bufferIN = new byte[Server.LunghezzaBuffer];
        
        
        boolean attivo = true;
        System.out.println("Server Attivato");
        while(attivo){
            DatagramPacket packet = new DatagramPacket(bufferIN, bufferIN.length);
            socket.receive(packet);
            Thread temp = new Thread(new Server(packet));
            temp.start();
        }
    }

}
