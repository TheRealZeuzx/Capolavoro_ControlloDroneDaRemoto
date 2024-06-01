package it.davincifascetti.controllosocketudp.program;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.command.CommandableException;

/**si occupa di prendere il pacchetto ricevuto dal server e elaborare la risposta corretta
 * Tramite la FactoryRisposta può instanziare comandi per la risposta di msg ricevuti dal client
 * @author Mussaldi Tommaso, Mattia Bonfiglio
 * @version 1.0
 */
public class ServerThread extends Thread implements Commandable{
    private DatagramPacket packet;
    private DatagramSocket socketRisposta;
    private byte[] bufferOUT = new byte[Server.LunghezzaBuffer];
    private DatagramPacket packetDaSpedire;


    private Server riferimentoServer = null;

    //! al momento non ha un proprio eventManager, utilizza quello di server perchè non credo necessiti di eventi propri
    /**instanzia una classe che si occupa della risposta
     * 
     * @param packet pacchetto ricevuto
     * @param socketRisposta socket che verra usata per la risposta (stessa del servers)
     * @param StoriaMsg storia dei msg ricevuti , server per instanziare 
     * @param riferimentoTerminal riferimento al terminale di server
     * @param nomeServerOriginale nome del server
     * @param fileLogger riferimento al file logger che si trova nel Server, serve per stampare su file
     * @param riferimentoServer riferimento all server che ha creato ServerThread
     */
    public ServerThread(DatagramPacket packet, DatagramSocket socketRisposta,Server riferimentoServer){
        this.packet = packet;
        this.socketRisposta = socketRisposta;
        this.riferimentoServer = riferimentoServer;

    }
    
    /**si occupa di instanziare la factory che si occupa di creare i comandi in base al tipo di risposta da inviare
     * 
     */
    @Override
    public void run() {
        
        this.riferimentoServer.getEventManager().notify(this.packet.getData(),this.packet.getLength(),this);
        
        
    }

    /**permette di inviare un messaggio di risposta al client (sa chi è dal pacchetto ricevuto)
     * 
     * @param msg messaggio da inviar al client
     * @throws CommandableException
     */
    public void inviaMsg(String msg) throws CommandableException{
        if(msg == null) throw new CommandableException("Errore, il messaggio da spedire risulta null");
		this.bufferOUT = msg.getBytes();
        InetAddress ClientIP = this.packet.getAddress();
        int ClientPort = this.packet.getPort();
        this.packetDaSpedire = new DatagramPacket(bufferOUT, bufferOUT.length,ClientIP,ClientPort);
		try {
			this.socketRisposta.send(this.packetDaSpedire);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }


    

    

    public DatagramPacket getPacket(){
        return this.packet;
    }


    
    

    @Override
    public EventManagerCommandable getEventManager() {
        return this.riferimentoServer.getEventManager();
    }

    public Server getServer(){return this.riferimentoServer;}



}



