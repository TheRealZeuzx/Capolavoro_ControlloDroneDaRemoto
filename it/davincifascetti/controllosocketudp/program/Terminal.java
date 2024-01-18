package it.davincifascetti.controllosocketudp.program;
import java.util.Scanner;

import it.davincifascetti.controllosocketudp.command.Command;
import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandFactory;
import it.davincifascetti.controllosocketudp.command.CommandFactoryInstantiator;
import it.davincifascetti.controllosocketudp.command.CommandHistory;
import it.davincifascetti.controllosocketudp.command.CommandStampaMsgRicevuti;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.command.ErrorLogCommand;
import it.davincifascetti.controllosocketudp.command.UndoableCommand;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLog;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
/**classe Terminal si occupa della gestione del terminale, utilizza i command per eseguire le operazioni richieste,
 * ha la possibilità di fare l'undo dei comandi che implementano UndoableCommand
 * 
 */
public class Terminal<T extends Commandable>{
    
    private CommandHistory storiaComandi; //lo uso solo per i comandi di creazione e delete dei server-client perché per le altre op non ha senso.
    private CommandFactory factory;
    private ErrorLog errorLog;
    private static Scanner input = new Scanner(System.in);
    private boolean attivo = false;

    public Terminal(ErrorLog errorLog) throws CommandException{
        this.errorLog = errorLog;
        this.storiaComandi = new CommandHistory();
    }

    public void main(T gestore) throws CommandException {
        this.attivo = true;
        this.factory = CommandFactoryInstantiator.newInstance(gestore);
        String menu;
        if(gestore instanceof GestoreClientServer) System.out.println("Terminale attivato \n\n--- Vista generale ---");
        if(gestore instanceof Server){
            System.out.println("Vista Server attivata");
            try {
                new CommandStampaMsgRicevuti((Server)gestore).execute();
            } catch (CommandException e) {
                System.out.println(e.getMessage());
            } catch (ErrorLogException e) {
                this.errorLog(e.getMessage(),true);
            }
        }
        if(gestore instanceof Client) System.out.println("Vista Client attivata");
        do{
            if(gestore instanceof GestoreClientServer) System.out.print(">");
            if(gestore instanceof Server) System.out.print("vs_>");
            if(gestore instanceof Client) System.out.print("vc_>");
            menu = input.nextLine();
            
            String[] params;
            if(menu.isBlank())
                params = null;
                //split crea un array in cui inserisce le parole separate dalla stringa inserita, in questo caso " "
            else params = menu.toLowerCase().split(" ");

            switch((params == null ? "" : params[0])){
            case "undo":
                    try {
                        if(!this.undo())System.out.println("non ci sono azioni significative da annullare");
                        else System.out.println("l'ultima azione significativa è stata annullata con successo");
                    } catch (CommandException e) {
                        System.out.println(e.getMessage());
                    } catch (ErrorLogException e) {
                        this.errorLog(e.getMessage(),true);
                    }
                break;
            case "quit":
                if(menu.equalsIgnoreCase("quit")){
                    if(gestore instanceof GestoreClientServer) System.out.println("Chiusura Programma ...");
                    if(gestore instanceof Server) System.out.println("Chiusura vista Server ...\n\n" + "--- Vista generale ---");
                    if(gestore instanceof Client) System.out.println("Chiusura vista Client ...\n\n" + "--- Vista generale ---");
                    break;
                }
            default:
                try{
                    this.executeCommand(factory.getCommand(params));
                }catch(CommandException e){
                    System.out.println(e.getMessage());
                }catch(ErrorLogException e){
                    this.errorLog(e.getMessage(),true);
                }
                break;
            }
        }while(!menu.equalsIgnoreCase("quit"));
        this.factory = null;
        this.attivo = false;
    }

    public void errorLog(String msg,boolean video){
        if(video)System.out.println(msg);
        new ErrorLogCommand(this.errorLog,msg).execute();
    }

    /**fa l'undo dell' ultimo undoableCommand che si trova nella storiaComandi, se non ci sono comandi allora non restituisce false
     * @return true se l'esecuzione è andata a buon fine altrimenti false
     * @throws ErrorLogException 
     * @throws CommandException 
     */
    private boolean undo() throws CommandException, ErrorLogException {
        if (storiaComandi.isEmpty()) return false;
        UndoableCommand command = storiaComandi.pop();
        if (command != null) {
            return command.undo();
        }
        return false;
    }

    /**esegue il comando e restituisce true se l'esec è riuscita altrimenti false, se il comando implementa undoable command, viene inserito nella storia comandi
     * 
     * @param command comando da eseguire (deve implementare Command)
     * @return true se l'esecuzione è andata a buon fine altrimenti false
     * @throws CommandException
     */
    private void executeCommand(Command command) throws CommandException,ErrorLogException{
        if(command == null) return;
        command.execute();
        if(command instanceof UndoableCommand)storiaComandi.push((UndoableCommand)command);
        
    }

    public boolean isAttivo(){return this.attivo;}
}
