package it.davincifascetti.controllosocketudp.program;



import java.util.Scanner;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandFactoryI;
import it.davincifascetti.controllosocketudp.command.CommandListManager;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;


public class Cli extends Component {

    private CommandFactoryI factory;
    private Commandable gestoreAttuale;
    private Commandable gestorePrecedente = null;



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
        System.out.print("--- Vista " + this.getGestoreAttuale().getClass().getSimpleName() +" ---\n"); //solo la prima volta
        do{ 
            if(!this.isAttivo(this.gestorePrecedente)){
                System.out.print("--- Vista " + this.getGestoreAttuale().getClass().getSimpleName() +" ---\n");
                this.gestorePrecedente = this.getGestoreAttuale();
            }
            System.out.print("(" + this.getGestoreAttuale().getClass().getSimpleName() +") >");
            menu ="";
            menu = input.nextLine();
            String[] params;
            if(menu.isBlank())
            params = null;
            //split crea un array in cui inserisce le parole separate dalla stringa inserita, in questo caso " "
            else params = menu.toLowerCase().split(" ");
            switch((params == null ? "" : params[0])){
                case "undo":
                try {
                    if(!this.undo(this.gestoreAttuale))System.out.println("non ci sono azioni significative da annullare");
                    else System.out.println("l'ultima azione significativa è stata annullata con successo");
                } catch (CommandException e) {
                    System.out.println(e.getMessage());
                } catch (ErrorLogException e) {
                    this.getUi().errorLog(e.getMessage(),true);
                }
                break;
                case "quit":
                System.out.println("Chiusura vista " + this.getGestoreAttuale().getClass().getSimpleName() + " ...\n");
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
            if(this.isAttivo(this.getUi().getBusiness()) && menu.equalsIgnoreCase("quit")){
                String conferma="";
                do{
                    System.out.print("sicuro di voler chiudere il programma? [y/n] : ");
                    conferma = input.nextLine();
                    if(conferma.equals("y"))System.out.println("Chiusura Programma ...");
                    else if(conferma.equals("n"))menu = "";
                }while(!conferma.equals("y") && !conferma.equals("n"));
            }
            else if(menu.equalsIgnoreCase("quit")){
                this.setVista(this.getUi().getBusiness());
                menu = "";

            }
        }while(!menu.equalsIgnoreCase("quit"));
    }
    
    public Commandable getGestoreAttuale(){return this.gestoreAttuale;}
    
    public boolean isAttivo(Commandable gestore){
        if(gestore == null) return false;
        return this.gestoreAttuale.equals(gestore);
    }


    @Override
    public void setManager (CommandListManager manager) throws CommandException{
        super.setManager(manager);
        this.factory.setManager(manager);
    }

    //TODO classe che gestisce la vista e tiene anche la factory relativa? + come si fa cambiarla dai command?
    /**cambia la vista attuale
     * 
     */
    public void setVista(Commandable gestore){
        this.gestorePrecedente = gestoreAttuale;
        this.gestoreAttuale = gestore;
        if(gestorePrecedente == null) this.gestorePrecedente = this.gestoreAttuale;
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
