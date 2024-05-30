package it.davincifascetti.controllosocketudp.program;



import java.util.Scanner;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandFactoryI;
import it.davincifascetti.controllosocketudp.command.CommandListManager;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;


public class Cli extends Component implements EventListenerRicezioneBuffer,EventListenerCommandable{

    private CommandListManager manager;
    private CommandFactoryI factory;
    private Commandable gestoreAttuale;
    
    private Ui riferimentoUi;


    public Cli(CommandListManager manager) throws CommandException{
        this.factory = new CommandFactoryI();
        this.setManager(manager);
    }
    public Cli(CommandListManager manager,Ui ui) throws CommandException{
        this.factory = new CommandFactoryI();
        this.setManager(manager);
        this.setUi(ui);
    }


    public void main(Scanner input){

        String menu;
        System.out.print("--- Vista " + this.getGestoreAttuale().getClass().getSimpleName() +" ---\n");
        do{ 
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
                    this.riferimentoUi.errorLog(e.getMessage(),true);
                }
                break;
                case "quit":
                System.out.println("Chiusura vista " + this.getGestoreAttuale().getClass().getSimpleName() + " ...\n");
                break;
                
                default:
                try{
                    this.executeCommand(factory.getCommand(this.getGestoreAttuale(),menu.toLowerCase(),this.riferimentoUi),this.getGestoreAttuale());
                }catch(CommandException e){
                    System.out.println(e.getMessage());
                }catch(ErrorLogException e){
                    this.riferimentoUi.errorLog(e.getMessage(),true);
                }
                break;
            }   
            if(this.isAttivo(this.riferimentoUi.getBusiness()) && menu.equalsIgnoreCase("quit")){
                String conferma="";
                do{
                    System.out.print("sicuro di voler chiudere il programma? [y/n] : ");
                    conferma = input.nextLine();
                    if(conferma.equals("y"))System.out.println("Chiusura Programma ...");
                    else if(conferma.equals("n"))menu = "";
                }while(!conferma.equals("y") && !conferma.equals("n"));
            }
        }while(!menu.equalsIgnoreCase("quit"));
    }
    
    public Commandable getGestoreAttuale(){return this.gestoreAttuale;}
    
    public boolean isAttivo(Commandable gestore){
        return this.gestoreAttuale.equals(gestore);
    }

    @Override
    public CommandListManager getManager() {
        return this.manager;
    }

    @Override
    public void setManager (CommandListManager manager) throws CommandException{
        super.setManager(manager);
        this.factory.setManager(manager);
    }

    @Override
    public void setUi(Ui ui) throws CommandException {
        if(ui == null) throw new CommandException("Errore, la UI passata è null!");
        this.riferimentoUi = ui;
    }

    //TODO classe che gestisce la vista e tiene anche la factory relativa? + come si fa cambiarla dai command?
    /**cambia la vista attuale
     * 
     */
    public void setVista(Commandable gestore){
        this.gestoreAttuale = gestore;
    }

    @Override
    public void update(byte[] buffer, int lung,Commandable commandable) {
        System.out.println("messaggio ricevuto!");
    }
    @Override
    public void update(String eventType, Commandable commandable) {
        System.out.println("è appena successa una cosa epica: " + eventType);
    }


    
}
