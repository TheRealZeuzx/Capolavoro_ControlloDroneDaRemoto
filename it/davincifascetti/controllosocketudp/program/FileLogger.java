package it.davincifascetti.controllosocketudp.program;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



/**classe che gestisce la scrittura su file
 * @author Mussaldi Tommaso, Mattia Bonfiglio
 * @version 1.0
*/
public class FileLogger{
    private File log;
    private FileWriter writer = null;
    private PrintWriter out = null;

    /**costruttore di filelogger
     * 
     * @param fileName nome del file su cui scrivere
     */
    public FileLogger(String fileName){
        this.log = new File(fileName);
    }

    /**permette di aprire lo stream, stampare su file e chiudere lo stream
     * 
     * @param messageToLog messaggio da loggare
     * @param mode true se modalità append altrimenti false
     * @throws IOException
     */
    public void printToFile(String messageToLog, boolean mode) throws IOException{
        openStream(mode);
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
