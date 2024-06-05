package it.davincifascetti.controllosocketudp.program;

import java.io.IOException;
import java.util.ArrayList;

import it.davincifascetti.controllosocketudp.command.CommandHistory;
import it.davincifascetti.controllosocketudp.command.CommandableException;

public class InfoServer {

    private CommandHistory storiaComandi;
    private FileLogger fileLogger= null;
    private ArrayList<String> storiaMsg;

    public InfoServer(){
        this.storiaComandi = new CommandHistory();
        this.storiaMsg = new ArrayList<>();
    }


    /**permette di impostare la stampa su file di default
     * 
     * @param append true se si vuole stampare in modalità append altrimenti false
     * @param nomeFile nome del file su cui si vuole scrivere, se ha valore "this" , il file prende il nome del server
     * @throws CommandableException se il nome del file è vuoto oppure null
     */
    public void setFileLogger(boolean append,String nomeFile,String nomeServer) throws CommandableException {
        
        if(nomeFile != null && nomeFile.equals("this")) this.fileLogger = new FileLogger(nomeServer + ".txt");
        else this.fileLogger = new FileLogger(nomeFile);
        this.fileLogger.setAppend(append);
        
    }

    /**permette di disabilitare la stampa su file di default
     * (non avviene se il fileLogger è null a meno che non si specifichi il comando $file, in quel caso il file prende il nome del server)
     */
    public void disableSuFile(){this.fileLogger = null;}

    public void clearMessaggi(){
        this.storiaComandi = null;
    }
    public CommandHistory getStoriaComandi(){return this.storiaComandi;}
    public ArrayList<String> getStoriaMsg(){return this.storiaMsg;}

        /**restitsce stringa contenente tutti i msg ricevuti dal client concatenati (aggiunge \n)
     * 
     * @return stringa contenente tutti i msg ricevuti dal client concatenati (aggiunge \n)
     */
    public String stampaStoriaMsg(Server s){
        String temp = "";   
        for (String string : this.storiaMsg) {
            temp +=string+"\n";
        }
        return temp;
    }

    public FileLogger getFileLogger() {
        return fileLogger;
    }

    //!non so se deve stare qui il filelogger
    /**permette di loggare su di un file che ha lo stesso nome del server, si occupa di aprire e terminale lo stream
     * se specificato non è specificato un file stampa sul file che prende il nome del server in modalità append
     * @param message messaggio da loggare
     * @return true se è andato a buonfine altrienti false
     * @throws CommandableException 
     */
    public boolean fileLog(String message,Server s) throws CommandableException{
        try{
            if(this.fileLogger == null){
                FileLogger logger = new FileLogger(s.getNome()+".txt");
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


    /**PER STAMPARE SUL TERMINALE SI USA QUESTO
     * se voglio stampare sul terminale devo usare questo metodo perchè in base a se il terminale è attivo o no , stampa a video oppure aggiunge alla lista storiamsg
     * @param msg messaggio da stampare
     */
    public void addMsg(String msg){
        this.storiaMsg.add(msg);
    }

}
