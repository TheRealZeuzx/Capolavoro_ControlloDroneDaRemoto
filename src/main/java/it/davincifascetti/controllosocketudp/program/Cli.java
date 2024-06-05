package it.davincifascetti.controllosocketudp.program;



import java.util.Scanner;
import java.util.Stack;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandFactoryI;
import it.davincifascetti.controllosocketudp.command.CommandListManager;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;


public class Cli extends Component {

    private CommandFactoryI factory;
    private Stack<Commandable> viste = new Stack<Commandable>();


    public Cli(CommandListManager manager) throws CommandException{
        this.factory = new CommandFactoryI();
        this.setManager(manager);
    }
    public Cli(CommandListManager manager,Ui ui) throws CommandException{
        this(manager);
        this.setUi(ui);
    }


    public void main(Scanner input){
        String menu;
        if(this.viste.empty()){ 
            System.out.println("Prima devi impostare una vista!");
            return; 
        }
        do{ 
            // System.out.println("\033[38;2;255;107;53m banner \u001B[0m");
            // System.out.println("\033[38;2;1;167;194m terminal \u001B[0m");
            // System.out.println("\033[38;2;239;239;208m scrivo \u001B[0m");
            // System.out.println("\033[38;2;144;103;198m risposta \u001B[0m");
            System.out.print("\033[38;2;1;167;194m"+"|" + this.getGestoreAttuale().getClass().getSimpleName() +"| >"  + "\u001B[0m");
            menu ="";
            System.out.print("\033[38;2;239;239;208m");//colore con cui scrivo in chat
            menu = input.nextLine();
            System.out.print("\033[38;2;144;103;198m");//colore risposta (metterlo nel metodo apposito)
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
                    System.out.println(e.getMessage());
                } catch (ErrorLogException e) {
                    this.getUi().errorLog(e.getMessage(),true);
                }
                break;
                case "quit":
                    if(this.isLastVista()){
                        String conferma="";
                        do{
                        
                            System.out.print("\033[38;2;1;167;194m|sure? [y/n]| > \033[38;2;239;239;208m");
                            conferma = input.nextLine();
                            if(conferma.equals("y"))System.out.println("\033[38;2;144;103;198mChiusura Programma ...");
                            else if(conferma.equals("n"))menu = "";
                        }while(!conferma.equals("y") && !conferma.equals("n"));
                    }else{
                        this.quitVista();
                        menu = "";
                    }
                break;
                case "clear":
                    System.out.print("\033\143");
                break;
                default:
                try{
                    this.executeCommand(factory.getCommand(this.getGestoreAttuale(),menu.toLowerCase(),this.getUi()),this.getGestoreAttuale());
                }catch(CommandException e){
                    System.out.println(e.getMessage());
                }catch(ErrorLogException e){
                    this.getUi().errorLog(e.getMessage(),true);
                }
                break;
            }   
        }while(!menu.equalsIgnoreCase("quit"));
        System.out.print("\u001B[0m");
    }
    
    public Commandable getGestoreAttuale(){return this.viste.peek();}
    
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

    public synchronized void stampa(){

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
