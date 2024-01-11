package it.davincifascetti.controllosocketudp.errorlog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import it.davincifascetti.controllosocketudp.program.FileLogger;



/**
    Classe Error permette di scrivere su di un file i log degli errori quando si sollevate eccezioni
    @author Mattia Bonfiglio - Tommaso Mussaldi
*/
public class ErrorLog {
    private FileLogger logger;

    /**costruttore di Error di default crea un file di log errori nel path del progetto.
     */
    public ErrorLog(){
        String fileName = "/errorLog.txt";
        this.logger = new FileLogger(fileName);
    }

    /**costruttore di Error che prende come parametro il path del file di log
     * 
     * @param fileName path del file di log su cui scrivere i log degli errori
     */
    public ErrorLog(String fileName){
        this.logger = new FileLogger(fileName);
    }
    

    /**permette la scrittura in modalità append sul file di log
     * 
     * @param errorMessage messaggio da scrivere in modalità append
     * @throws IOException eccezione sollevata dal metodo write di PrintWriter 
     */
    public void log(String errorMessage) throws IOException{
        this.logger.printToFile(errorMessage,true);
    }


}
