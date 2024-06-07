package it.davincifascetti.controllosocketudp.program;



import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandFactoryI;
import it.davincifascetti.controllosocketudp.command.CommandListManager;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;


public class Cli extends Component {

    private CommandFactoryI factory;
    private Stack<Commandable> viste = new Stack<Commandable>();
    private AtomicBoolean BLOCCATO = new AtomicBoolean(); //rende boolean thread safe
    private Scanner input = null;
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
        if(this.isBloccato()) return;
        if(this.input == null){ 
            System.out.println("Prima devi specificare uno scanner!");
            return; 
        }
        if(this.viste.empty()){ 
            System.out.println("Prima devi impostare una vista!");
            return; 
        }
        String menu;
        do{ 
            if(this.isBloccato()) return;
            System.out.print(SCOPE_COLOR+"|" + this.getGestoreAttuale().getClass().getSimpleName() +"| >"  + RESET_COLOR);
            menu ="";
            System.out.print(INPUT_COLOR);
            menu = input.nextLine();
            System.out.print(OUTPUT_COLOR);
            String[] params;
            if(menu.isBlank())
            params = null;
            //split crea un array in cui inserisce le parole separate dalla stringa inserita, in questo caso " "
            else params = menu.toLowerCase().split(" ");
            switch((params == null ? "" : params[0])){
                case "undo":
                try {
                    if(!this.undo(this.getGestoreAttuale()))System.out.println("no commands to undo");
                    else System.out.println("last command undone correctly");
                } catch (CommandException e) {
                    this.printError(e.getMessage());
                } catch (ErrorLogException e) {
                    this.printError(e.getMessage());
                    this.getUi().errorLog(e.getMessage());
                }
                break;
                case "quit":
                    if(this.isLastVista()){
                        String conferma="";
                        do{
                        
                            System.out.print(SCOPE_COLOR + "|sure? [y/n]| > " + INPUT_COLOR);
                            conferma = input.nextLine();
                            if(conferma.equals("y"))System.out.println(OUTPUT_COLOR+"Chiusura Programma ...");
                            else if(conferma.equals("n"))menu = "";
                        }while(!conferma.equals("y") && !conferma.equals("n"));
                    }else{
                        this.quitVista();
                        menu = "";
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
                    this.getUi().errorLog(e.getMessage());
                }catch(Exception e){
                    this.printError(e.getMessage());
                }
                break;
            }   
        }while(!menu.equalsIgnoreCase("quit"));
        System.out.print(RESET_COLOR);
    }
    
    public Commandable getGestoreAttuale(){return this.viste.peek();}
    //TODO capire come gestire il "bloccaggio" della CLI, esempio quando devo stampare un msg dall'esterno ma sto in input
    public synchronized boolean isBloccato(){return this.BLOCCATO.get();}
    public synchronized void setBloccato(boolean bloccato){
        this.BLOCCATO.set(bloccato);
        if(!this.isBloccato()) this.main();
    }

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
        if(!this.isBloccato())
            System.out.println(OUTPUT_COLOR  + message);
        System.out.print(RESET_COLOR);
    }
    /**PER STAMPARE UN ERRORE SULLA CLI SI USA QUESTO (chiunque voglia stampare a video deve usare questo, quindi lo gestirà la UI chi stampa cosa)
     * 
     * @param message
     */
    public synchronized void printError(String message){
        System.out.println(ERROR_COLOR  + message);
        System.out.print(RESET_COLOR);
    }

    public void setScanner(Scanner input) throws CommandException{
        if(input == null) throw new CommandException("Errore, scanner è null!");
        this.input = input;
    }

    // @Override
    // public void update(byte[] buffer, int lung,Commandable commandable) {
    //     System.out.println(commandable.getClass().getSimpleName() + ": messaggio ricevuto! --> " + new String(buffer));
    // }
    // @Override
    // public void update(String eventType, Commandable commandable) {
    //     switch (eventType) {
    //         case Client.SERVER_NO_RESPONSE:
    //             System.out.println("Il server non ha dato nessuna risposta =( ");
    //             break;
        
    //         default:
    //             System.out.println("è appena successa una cosa epica: " + eventType);
    //             break;
    //     }
       
    // }


    
}
