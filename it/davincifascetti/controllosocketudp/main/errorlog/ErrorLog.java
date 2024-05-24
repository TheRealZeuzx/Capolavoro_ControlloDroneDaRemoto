package it.davincifascetti.controllosocketudp.errorlog;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.program.FileLogger;



/**
    Classe Error permette di scrivere su di un file i log degli errori quando si sollevate eccezioni
    @author Mattia Bonfiglio - Tommaso Mussaldi
*/
public class ErrorLog {
    private FileLogger logger;

    /**costruttore di Error di default crea un file di log errori nel path del progetto.
     * @throws CommandableException 
     */
    public ErrorLog() throws CommandableException{
        String fileName = "/errorLog.txt";
        this.logger = new FileLogger(fileName);
        this.logger.setAppend(true);
    }

    /**costruttore di Error che prende come parametro il path del file di log
     * 
     * @param fileName path del file di log su cui scrivere i log degli errori
     * @throws CommandableException 
     */
    public ErrorLog(String fileName) throws CommandableException{
        this.logger = new FileLogger(fileName);
        this.logger.setAppend(true);
    }
    

    /**permette la scrittura in modalità append sul file di log
     * 
     * @param errorMessage messaggio da scrivere in modalità append
     * @throws IOException eccezione sollevata dal metodo write di PrintWriter 
     */
    public void log(String errorMessage) throws IOException{
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        this.logger.printToFile("[" + dtf.format(now) + "]\t"+errorMessage); //stampo il msg errore + la data 
    }


}
