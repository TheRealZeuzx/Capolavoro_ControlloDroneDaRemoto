package it.davincifascetti.controllosocketudp.program;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandFactoryI;
import it.davincifascetti.controllosocketudp.command.CommandListManager;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;


public class Cli extends Component implements Runnable{

    private CommandFactoryI factory;
    private Stack<Commandable> viste = new Stack<Commandable>();
    private Scanner input = null;
    private Thread esecuzione = null;
    //STATI DELLA CLI
    private final AtomicBoolean LOCKED = new AtomicBoolean(); //se la cli deve essere bloccata (no richiesta input)
    private final AtomicBoolean RUNNING = new AtomicBoolean(); //se al momento è in esecuzione
    private final AtomicBoolean PRINT_ENABLED = new AtomicBoolean(); //se al momento la stampa a video (metodo print e printError) sono attivi
    private final AtomicBoolean GETTING_INPUT = new AtomicBoolean(); //se al momento mi trovo bloccato nella richiesta input
    private final AtomicBoolean TERMINATED = new AtomicBoolean(); //se viene terminata
    //COLOR CODES CONSOLE
    public final static String BANNER_COLOR = "\033[38;2;255;107;53m";
    public final static String INPUT_COLOR = "\033[38;2;239;239;208m";
    public final static String OUTPUT_COLOR = "\033[38;2;130;112;129m";
    public final static String SCOPE_COLOR = "\033[38;2;1;167;194m"; // si riferisce a |gestore|>
    public final static String ERROR_COLOR = "\033[38;2;226;61;40m";
    
    public final static String RESET_COLOR = "\u001B[0m";
    public final static String CLEAR_CONSOLE = "\033\143";

    public Cli(CommandListManager manager) throws CommandException{
        this.factory = new CommandFactoryI();
        this.setManager(manager);
    }
    public Cli(CommandListManager manager,Ui ui) throws CommandException{
        this(manager);
        this.setUi(ui);
    }


    public void main(){
        if(this.input == null){  
            this.printError("Prima devi specificare uno scanner!");
            return; 
        }
        if(this.viste.empty()){ 
            this.printError("Prima devi impostare una vista!");
            return; 
        }
        this.esecuzione = new Thread(this);
        this.esecuzione.start();
    }
    
    @Override
    public void run(){
        do{
            this.loop();
            try {
                synchronized(this.esecuzione){
                    this.esecuzione.wait(20);
                }
            } catch (InterruptedException e) {
                this.printError(e.getMessage());
            }
        }while(!this.isTerminated());
        this.print("Chiusura Programma ...");
        this.getUi().kill();//destroy di tutto
    }

    private void loop(){
        if(this.isLocked()){
            return;
        }
        String menu;
        synchronized(GETTING_INPUT){
            GETTING_INPUT.compareAndSet(false, true);
            System.out.print(SCOPE_COLOR+"|" + this.getGestoreAttuale().getClass().getSimpleName() +"| >");
            System.out.print(INPUT_COLOR + ">" );
            menu ="";
            menu = input.nextLine();
            System.out.print(OUTPUT_COLOR);
            GETTING_INPUT.compareAndSet(true, false);
        }
        String[] params;
        if(menu.isBlank())
        params = null;
        //split crea un array in cui inserisce le parole separate dalla stringa inserita, in questo caso " "
        else params = menu.toLowerCase().split(" ");
        switch((params == null ? "" : params[0])){
            case "undo":
            try {
                if(!this.undo(this.getGestoreAttuale()))System.out.println(OUTPUT_COLOR + "no commands to undo");
                else System.out.println(OUTPUT_COLOR + "last command undone correctly");
            } catch (CommandException e) {
                this.printError(e.getMessage());
            } catch (ErrorLogException e) {
                this.printError(e.getMessage());
                this.getUi().fileErrorLog(e.getMessage());
            }
            break;
            case "quit":
                if(this.isLastVista()){
                    String conferma="";
                    do{
                        synchronized(GETTING_INPUT){

                            System.out.print(SCOPE_COLOR + "|sure? [y/n]| > " + INPUT_COLOR);
                            if(!this.GETTING_INPUT.get()){
                                this.GETTING_INPUT.set(true);
                                conferma = input.nextLine();
                            }
                            this.GETTING_INPUT.set(false);
                        }
                    }while(!conferma.equals("y") && !conferma.equals("n"));
                    if(conferma.equals("y")) {
                        this.setTerminated();
                    }
                }else{
                    this.quitVista();
                }
            break;
            case "clear":
                System.out.print(CLEAR_CONSOLE);
            break;
            default:
            try{
                this.executeCommand(factory.getCommand(this.getGestoreAttuale(),menu.toLowerCase(),this.getUi()),this.getGestoreAttuale());
            }catch(CommandException e){
                this.printError(e.getMessage());
            }catch(ErrorLogException e){
                this.printError(e.getMessage());
                this.getUi().fileErrorLog(e.getMessage());
            }catch(Exception e){
                this.printError(e.getMessage());
            }
            break;
        }
    }

    public Commandable getGestoreAttuale(){return this.viste.peek();}
    public synchronized boolean isLocked(){return this.LOCKED.get();}
    public synchronized void setLocked(boolean bloccato){
        this.LOCKED.set(bloccato);
    }
    public synchronized boolean isTerminated(){return this.TERMINATED.get();}
    public synchronized void setTerminated(){
        this.TERMINATED.set(true);
    }
    public synchronized boolean isPrintEnabled(){return this.PRINT_ENABLED.get();}
    public synchronized void setPrintEnabled(boolean enable){
        this.PRINT_ENABLED.set(enable);
    }
    public synchronized boolean isRunning(){return this.RUNNING.get();}
    public synchronized void setRunning(boolean running){
        this.PRINT_ENABLED.set(running);
    }

    /**si riferisce alla view (se il commandable è attivo significa che è la view attuale )
     * 
     * @param gestore
     * @return
     */
    public boolean isAttivo(Commandable gestore){
        if(gestore == null) return false;
        return this.viste.peek().equals(gestore);
    }


    @Override
    public void setManager (CommandListManager manager) throws CommandException{
        super.setManager(manager);
        this.factory.setManager(manager);
    }

    //TODO classe che gestisce la vista e tiene anche la factory relativa? + come si fa cambiarla dai command?
    /**cambia la vista attuale, aggiunge allo stack e userà quello come gestore attuale
     * 
     */
    public void setVista(Commandable gestore){
        this.viste.add(gestore);
    }
    /**permette di tornare indietro nello stack
     * 
     * @return il gestore quittato
     */
    public Commandable quitVista(){
        return this.viste.pop();
    }
    /**restituisce true se è rimasto una sola vista (mi trovo nel root element)
     * 
     * @return true se sono all gestore root
     */
    public boolean isLastVista(){
        return this.viste.size() > 1 ? false : true;
    }

    /**PER STAMPARE SULLA CLI SI USA QUESTO (chiunque voglia stampare a video deve usare questo, quindi lo gestirà la UI chi stampa cosa)
     * 
     * @param message
     */
    public synchronized void print(String message){

        if(!this.GETTING_INPUT.get()){
            System.out.println(OUTPUT_COLOR  + message);
            System.out.print(RESET_COLOR);
        }else{
            System.out.println("\r" + OUTPUT_COLOR  + message);
            System.out.print(SCOPE_COLOR+"|" + this.getGestoreAttuale().getClass().getSimpleName() +"| >");
            System.out.print(INPUT_COLOR + ">" );
        }
        
        
        
    }
    /**PER STAMPARE UN ERRORE SULLA CLI SI USA QUESTO (chiunque voglia stampare a video deve usare questo, quindi lo gestirà la UI chi stampa cosa)
     * 
     * @param message
     */
    //TODO gestire il bloccaggio
    public synchronized void printError(String message){
        
        if(!this.GETTING_INPUT.get()){
            System.out.println(ERROR_COLOR  + message);
            System.out.print(RESET_COLOR);
        }else{
            System.out.println("\r" + OUTPUT_COLOR  + message);
            System.out.print(SCOPE_COLOR+"|" + this.getGestoreAttuale().getClass().getSimpleName() +"| >");
            System.out.print(INPUT_COLOR + ">" );
        }
 
    }

    public void setScanner(Scanner input) throws CommandException{
        if(input == null) throw new CommandException("Errore, scanner è null!");
        this.input = input;
    }
    
    @Override
    public void destroy(){
        super.destroy();
        this.viste = null;
        this.factory = null;

    }
    
}
