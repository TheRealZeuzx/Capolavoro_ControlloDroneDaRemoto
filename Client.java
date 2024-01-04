
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {

	public static void main(String[] args) throws IOException {

		String host ="localhost"; //host a cui inviare (indirizzo ip)
		int portaServer = 8890;//porta tcp/udp
		
		InetAddress ipServer = InetAddress.getByName(host); //server ip (il local host)
				
		byte[] bufferIN = new byte[1024];
		byte[] bufferOUT = new byte[1024];
		
		//input stream
		BufferedReader  input = new BufferedReader(new InputStreamReader(System.in));
		//creo il socket client
		DatagramSocket client = new DatagramSocket();
		
		String daSpedire = "";
		System.out.println("Client Pronto");
		//ciclo client ( continua finche non scrivo fine)
		do {
			//prendo in input un msg da testo e lo trasformo in bytes (lo inserisco in bufferOUT)
			System.out.println("inserisci il msg da inviare: ");
			daSpedire = input.readLine();
			bufferOUT = daSpedire.getBytes();
			
			//creo il  datagram e inserisco il bufferOUT e ipServer e portaServer)
			DatagramPacket p = new DatagramPacket(bufferOUT,bufferOUT.length,ipServer,portaServer);
			//invio il pacchetto
			for(int i = 0;i<10;i++){
                client.send(p);
            }

			


			if(!daSpedire.equalsIgnoreCase("fine")) {
				//creo il pacchetto di ricezione
				DatagramPacket ricevuto = new DatagramPacket(bufferIN,bufferIN.length);
				//ricevo sul pacchetto di ricezione
				client.receive(ricevuto);
				//creo stringa ricevuta dall buffer di dati ricevuti
				String msgRicevuto = new String(ricevuto.getData());
				
				msgRicevuto = msgRicevuto.substring(0, ricevuto.getLength());
				System.out.println("RICEVUTO: " + msgRicevuto);
			}
			
		}while(!daSpedire.equalsIgnoreCase("fine"));
		System.out.println("client disattivato");
		client.close();
		
	}

}

