package it.davincifascetti.controllosocketudp.program;
import java.util.Scanner;

import it.davincifascetti.controllosocketudp.command.Command;
import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandFactory;
import it.davincifascetti.controllosocketudp.command.CommandFactoryInstantiator;
import it.davincifascetti.controllosocketudp.command.CommandHistory;
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
        System.out.println("Terminale attivato");
        do{
            System.out.print(">");
            menu = input.nextLine();
            
            String[] params;
            if(menu.isBlank())
                params = null;
                //split crea un array in cui inserisce le parole separate dalla stringa inserita, in questo caso " "
            else params = menu.toLowerCase().split(" ");

            switch((params == null ? "" : params[0])){
            case "undo":
                if(!this.undo())System.out.println("non ci sono azioni significative da annullare");
                else System.out.println("l'ultima azione significativa è stata annullata con successo");
                break;
            case "quit":
                if(menu.equalsIgnoreCase("quit")){
                    System.out.println("Terminale chiuso");
                    break;
                }
            default:
                try{
                    this.executeCommand(factory.getCommand(params));
                }catch(CommandException e){
                    System.out.println(e.getMessage());
                }catch(ErrorLogException e){
                    new ErrorLogCommand(this.errorLog,e.getMessage()).execute();
                }
                break;
            }
        }while(!menu.equalsIgnoreCase("quit"));
        input.close();
        this.factory = null;
        this.attivo = false;
    }



    /**fa l'undo dell' ultimo undoableCommand che si trova nella storiaComandi, se non ci sono comandi allora non restituisce false
     * @return true se l'esecuzione è andata a buon fine altrimenti false
     */
    private boolean undo() {
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
