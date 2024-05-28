package it.davincifascetti.controllosocketudp.program;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import it.davincifascetti.controllosocketudp.command.Command;
import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandFactoryI;
import it.davincifascetti.controllosocketudp.command.CommandHistory;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.command.UndoableCommand;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;

/**si occupa di prendere il pacchetto ricevuto dal server e elaborare la risposta corretta
 * Tramite la FactoryRisposta può instanziare comandi per la risposta di msg ricevuti dal client
 * @author Mussaldi Tommaso, Mattia Bonfiglio
 * @version 1.0
 */
public class ServerThread extends Thread implements Commandable{
    private DatagramPacket packet;
    private DatagramSocket socketRisposta;
    private ArrayList<String> StoriaMsg;
    private byte[] bufferOUT = new byte[Server.LunghezzaBuffer];
    private DatagramPacket packetDaSpedire;
    private CommandHistory storiaComandi;
    private String msgRicevuto = "";
    private String nomeServerOriginale;
    private FileLogger fileLogger = null;
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
    public ServerThread(DatagramPacket packet, DatagramSocket socketRisposta, ArrayList<String> StoriaMsg,String nomeServerOriginale, FileLogger fileLogger,Server riferimentoServer){
        this.packet = packet;
        this.socketRisposta = socketRisposta;
        this.StoriaMsg = StoriaMsg;

        this.msgRicevuto = this.getMsgRicevuto();
        this.nomeServerOriginale = nomeServerOriginale;
        this.fileLogger = fileLogger;
        this.riferimentoServer = riferimentoServer;

    }
    
    /**si occupa di instanziare la factory che si occupa di creare i comandi in base al tipo di risposta da inviare
     * 
     */
    @Override
    public void run() {
        this.getEventManager().notify(msgRicevuto);

        //! il codice sotto lo eseguirà la CLI o chi per lui
        if(factory != null){
            try{
                this.executeCommand(factory.getCommand());
            }catch(CommandException e){
                this.stampaVideo(e.getMessage());
            }catch(ErrorLogException e){
                this.errorLog(e.getMessage(), true);
            }
        }
        
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


    /**PER STAMPARE SUL TERMINALE SI USA QUESTO
     * se voglio stampare sul terminale devo usare questo metodo perchè in base a se il terminale è attivo o no , stampa a video oppure aggiunge alla lista storiamsg
     * @param msg messaggio da stampare
     */
    public void stampaVideo(String msg){
        if(this.riferimentoTerminal.isAttivo(this.riferimentoServer))System.out.println(msg);
		else this.StoriaMsg.add(msg);
    }

    /**permette di loggare un errore 
     * 
     * @param msg messaggio da loggare
     * @param video true se si stampa anche a video altrimenti false  solo su file
     */
    private void errorLog(String msg, boolean video){
        if(video)this.stampaVideo(msg);
        this.riferimentoTerminal.errorLog(msg,false);
    }

    /**permette di estrapolare il messaggio ricevuto dal pacchetto ricevuto
     * 
     * @return stringa contenente il msg ricevuto dal client
     */
    private String getMsgRicevuto(){
        int lungPacket = this.packet.getLength();
		String msgRicevuto = new String(this.packet.getData());
		return msgRicevuto.substring(0, lungPacket);
        
    }

    public DatagramPacket getPacket(){
        return this.packet;
    }

    /**esegue il comando e restituisce true se l'esec è riuscita altrimenti false, se il comando implementa undoable command, viene inserito nella storia comandi
     * 
     * @param command comando da eseguire (deve implementare Command)
     * @return true se l'esecuzione è andata a buon fine altrimenti false
     * @throws CommandException
     */
    private void executeCommand(Command command) throws CommandException,ErrorLogException{
        if(command == null) return;
        command.execute();
        if(command instanceof UndoableCommand)storiaComandi.push((UndoableCommand)command);
        
    }
    
    /**permette di loggare su di un file che ha lo stesso nome del server, si occupa di aprire e terminale lo stream
     * se specificato non è specificato un file stampa sul file che prende il nome del server in modalità append
     * @param message messaggio da loggare
     * @return true se è andato a buonfine altrienti false
     * @throws CommandableException 
     */
    public boolean fileLog(String message) throws CommandableException{
        try{
            if(this.fileLogger == null){
                FileLogger logger = new FileLogger(nomeServerOriginale+".txt"); //TODO farglielo stampare dentro un apposita cartella (non funziona ora) (credo che i packages vadano messi in una cartella src e poi fuori la cartella fileServers)
                logger.setAppend(true);
                logger.printToFile(message);
                return true;
            }
            this.fileLogger.printToFile(message);
            return true;
        }catch(IOException e){
            return false;
        }

    }

    /** operazione da eseguire in caso venga ricevuta una stringa senza comando (senza '$')
     * 
     * @param message messaggio ricevuto
     * @throws CommandableException
     */
    public void defaultResponse(String message) throws CommandableException{
        if(this.fileLogger != null)
            this.fileLog(msgRicevuto);
        this.stampaVideo("il client dice: " + message);
    }

    @Override
    public EventManagerCommandable getEventManager() {
        return this.riferimentoServer.getEventManager();
    }




}



