package it.davincifascetti.controllosocketudp.program;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import it.davincifascetti.controllosocketudp.command.Command;
import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandFactoryRisposta;
import it.davincifascetti.controllosocketudp.command.CommandHistory;
import it.davincifascetti.controllosocketudp.command.UndoableCommand;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;

/**si occupa di prendere il pacchetto ricevuto dal server e elaborare la risposta corretta
 * di default stampaVideo è attivo quindi stampa a video tutti i msg ricevuti dal client
 * 
 */
public class ServerThread implements Runnable{
    private DatagramPacket packet;
    private DatagramSocket socketRisposta;
    private ArrayList<String> StoriaMsg;
    private Terminal<Server> riferimentoTerminal;
    private byte[] bufferOUT = new byte[Server.LunghezzaBuffer];
    private DatagramPacket packetDaSpedire;
    private CommandHistory storiaComandi;
    private String msgRicevuto = "";
    private String nomeServerOriginale;
    //TODO creare i seguenti metodi per eseguire le op di :
    //TODO stampa msg del  client sulla console (se il terminale è attivo altrimenti li salva su storiamsg)
    //TODO salva su file msg client

    public ServerThread(DatagramPacket packet, DatagramSocket socketRisposta, ArrayList<String> StoriaMsg, Terminal<Server> riferimentoTerminal,CommandHistory storiaComandi, String nomeServerOriginale){
        this.packet = packet;
        this.socketRisposta = socketRisposta;
        this.StoriaMsg = StoriaMsg;
        this.riferimentoTerminal = riferimentoTerminal;
        this.storiaComandi = storiaComandi;
        InetAddress ClientIP = this.packet.getAddress();
        int ClientPort = this.packet.getPort();
		this.packetDaSpedire = new DatagramPacket(bufferOUT, bufferOUT.length,ClientIP,ClientPort);
        this.msgRicevuto = this.getMsgRicevuto();
        this.nomeServerOriginale = nomeServerOriginale;
    }

    @Override
    public void run() {

        CommandFactoryRisposta factory = null; 
        try {
            factory = new CommandFactoryRisposta(this);
        } catch (CommandException e) {
            this.StoriaMsg.add(e.getMessage());
        }
        if(factory != null){
            String[] temp = new String[2];
            String[] params;
            if(this.msgRicevuto.isBlank())params = null;else params = this.msgRicevuto.toLowerCase().split("-");
            switch((params == null ? "" : params[0])){
            case "undo":
                    try {
                        if(!this.undo())this.stampaVideo("non ci sono azioni significative da annullare");
                        else this.stampaVideo("l'ultima azione significativa è stata annullata con successo");
                    }catch(CommandException e){
                        this.stampaVideo(e.getMessage());
                    }catch(ErrorLogException e){
                        this.errorLog(e.getMessage(), true);
                    }
                break;
            default:
                //! se non torna forse è questo (deve prendere il secondo parametro che è il msg senza il comand quindi es: "log-")
                temp[1] = this.msgRicevuto.substring(params[0].length(),this.msgRicevuto.length() - 1);
                temp[0] = temp[1].isBlank() ? "" : params[0];
                try{
                    this.executeCommand(factory.getCommand(temp));
                }catch(CommandException e){
                    this.stampaVideo(e.getMessage());
                }catch(ErrorLogException e){
                    this.errorLog(e.getMessage(), true);
                }
                break;
            }
        }
        

    }

    public void inviaMsg(String msg){
        String daSpedire = this.getMsgRicevuto().toUpperCase();
		bufferOUT = daSpedire.getBytes();
		try {
			this.socketRisposta.send(this.packetDaSpedire);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
    }


    /**PER STAMPARE SUL TERMINALE SI USA QUESTO
     * 
     * @param msg
     */
    public void stampaVideo(String msg){
        if(this.riferimentoTerminal.isAttivo())System.out.println(msg);
		else this.StoriaMsg.add(msg);
    }

    private void errorLog(String msg, boolean video){
        if(video)this.stampaVideo(msg);
        this.riferimentoTerminal.errorLog(msg,false);
    }

    private String getMsgRicevuto(){
        int lungPacket = this.packet.getLength();
		String msgRicevuto = new String(this.packet.getData());
		return msgRicevuto.substring(0, lungPacket);
        
    }

    public DatagramPacket getPacket(){
        return this.packet;
    }

     /**fa l'undo dell' ultimo undoableCommand che si trova nella storiaComandi, se non ci sono comandi allora non restituisce false
     * @return true se l'esecuzione è andata a buon fine altrimenti false
     * @throws ErrorLogException 
     * @throws CommandException 
     */
    private boolean undo() throws CommandException, ErrorLogException {
        if (storiaComandi.isEmpty()) return false;
        UndoableCommand command = storiaComandi.pop();
        if (command != null) {
            return command.undo();
        }
        return false;
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
    
    public boolean fileLog(String message){
        try{
            FileLogger logger = new FileLogger(nomeServerOriginale);
            logger.printToFile(message, true);
            return true;
        }catch(IOException e){
            return false;
        }

    }
}



