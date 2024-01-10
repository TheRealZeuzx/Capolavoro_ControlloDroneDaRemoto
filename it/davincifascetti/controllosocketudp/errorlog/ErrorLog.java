import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



/**
    Classe Error permette di scrivere su di un file i log degli errori quando si sollevate eccezioni
    @author Mattia Bonfiglio - Tommaso Mussaldi
*/
public class ErrorLog {
    private File log;
    private FileWriter writer = null;
    private PrintWriter out = null;

    /**costruttore di Error di default crea un file di log errori nel path del progetto.
     */
    public ErrorLog(){
        String fileName = "/errorLog.txt";
        this.log = new File(fileName);
    }

    /**costruttore di Error che prende come parametro il path del file di log
     * 
     * @param fileName path del file di log su cui scrivere i log degli errori
     */
    public ErrorLog(String fileName){
        this.log = new File(fileName);
    }
    

    /**permette la scrittura in modalità append sul file di log
     * 
     * @param errorMessage messaggio da scrivere in modalità append
     * @throws IOException eccezione sollevata dal metodo write di PrintWriter 
     */
    public void appendToStream(String errorMessage) throws IOException{
        openStream(true);
        this.out.write(errorMessage);
        closeStream();
        
    }

    /** permette l'apertura del file log
     * 
     * @param append true se la scrittura deve essere in modalità append altrimenti false
     * @throws IOException eccezione sollevata da FileWriter
     */
    private void openStream(boolean append) throws IOException{

		this.writer = new FileWriter(this.log,append);
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
