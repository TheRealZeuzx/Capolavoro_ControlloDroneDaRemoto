package it.davincifascetti.controllosocketudp.program;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import it.davincifascetti.controllosocketudp.command.CommandableException;



/**classe che gestisce la scrittura su file
 * @author Mussaldi Tommaso, Mattia Bonfiglio
 * @version 1.0
*/
public class FileLogger{
    private File log;
    private FileWriter writer = null;
    private PrintWriter out = null;
    private boolean append = false;

    public String getFileName(){
        return log.getName();
    }
    /**
     * 
     * @return restituisce true se la modalità append è impostata altrimenti false
     */
    public boolean isAppend() {
        return append; 
    }
    /**la modalità append di default è false
     * 
     * @param append se si decide di impostare la scrittura in modalità append
     */
    public void setAppend(boolean append) {
        this.append = append;
    }

    /**costruttore di filelogger
     * 
     * @param fileName nome del file su cui scrivere
     * @throws CommandableException  se il nome è null oppure vuoto
     */
    public FileLogger(String fileName) throws CommandableException{
        try{
            if(fileName.isBlank()) throw new CommandableException("non è stato inserito nessun nome");
            this.log = new File(fileName);
        }catch(NullPointerException e){throw new CommandableException("non è stato inserito nessun nome");}
    }

    /**permette di aprire lo stream, stampare su file e chiudere lo stream
     * se si vuole stampare in modalita append bisogna impostare usando il setter di append
     * @param messageToLog messaggio da loggare
     * @throws IOException
     */
    public void printToFile(String messageToLog) throws IOException{
        openStream(this.append);
        this.out.println(messageToLog);
        closeStream();
    }

    /**apre lo stream del file
     * 
     * @param mode true se modalità append altrimenti false
     * @throws IOException
     */
    private void openStream(boolean mode) throws IOException{
		this.writer = new FileWriter(this.log,mode);
		this.out = new PrintWriter(writer);
    }

    /**permette la chiusura del file
     * 
     * @throws IOException eccezione sollevata dalla chiusura di FileWriter
     */
    private void closeStream() throws IOException{
        this.writer.close();
		this.out.close();
    }


}
