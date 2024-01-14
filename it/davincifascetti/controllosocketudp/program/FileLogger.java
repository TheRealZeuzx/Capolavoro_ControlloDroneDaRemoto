package it.davincifascetti.controllosocketudp.program;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



/**
    
    @author Mattia Bonfiglio - Tommaso Mussaldi
*/
public class FileLogger{
    private File log;
    private FileWriter writer = null;
    private PrintWriter out = null;


    public FileLogger(String fileName){
        this.log = new File(fileName);
    }


    public void printToFile(String messageToLog, boolean mode) throws IOException{
        openStream(mode);
        this.out.println(messageToLog);
        closeStream();
    }

    private void openStream(boolean mode) throws IOException{
		this.writer = new FileWriter(this.log,mode);
		this.out = new PrintWriter(writer);
    }

    /**permette la chiusura del file log
     * 
     * @throws IOException eccezione sollevata dalla chiusura di FileWriter
     */
    private void closeStream() throws IOException{
        this.writer.close();
		this.out.close();
    }


}
