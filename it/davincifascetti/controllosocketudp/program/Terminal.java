package it.davincifascetti.controllosocketudp.program;
import java.util.Scanner;

import it.davincifascetti.controllosocketudp.command.Command;
import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandFactoryI;
import it.davincifascetti.controllosocketudp.command.CommandHistory;
import it.davincifascetti.controllosocketudp.command.CommandListManager;
import it.davincifascetti.controllosocketudp.command.CommandStampaMsgRicevuti;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.command.ErrorLogCommand;
import it.davincifascetti.controllosocketudp.command.UndoableCommand;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLog;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
/**classe Terminal si occupa della gestione del terminale, utilizza i command per eseguire le operazioni richieste,
 * ha la possibilità di fare l'undo dei comandi che implementano UndoableCommand
 * instanzia una commandFactory corrispondente al gestore utilizzando CommandFactoryInstantiar 
 * la command factory si occupa di creare i comandi opportuni per ogni messaggio inviato dal utente, la execute del command , attivera il receiver che eseguira effettivaemnte l'operazione richiesta
 * utilizza il command design pattern
 * @author Mussaldi Tommaso, Mattia Bonfiglio
 * @version 1.0
 */
public class Terminal<T extends Commandable>{
    
    private CommandHistory storiaComandi; //lo uso solo per i comandi di creazione e delete dei server-client perché per le altre op non ha senso.
    private CommandFactoryI<T> factory;
    private ErrorLog errorLog;
    public static Scanner input = new Scanner(System.in);
    private boolean attivo = false;
    private boolean bloccato = false;
    private T gestoreAttuale = null;
    private CommandListManager manager = null;
    private GestoreRemote telecomandi = null;

    /**
     * 
     * @param errorLog oggetto error log che si occupera del log su file degli errori
     * @throws CommandException
     */
    public Terminal(ErrorLog errorLog,CommandListManager manager) throws CommandException{
        if(errorLog == null || manager == null) throw new CommandException("Errore, qualcosa è andato storto!");
        this.errorLog = errorLog;
        this.manager = manager;
        this.storiaComandi = new CommandHistory();
    }

    /**peremtte di avviare il terminale, se bloccato è true gli input sono bloccati, se attivo allora attivo = true altrimenti false , serve a capire se il terminale è attivo dall'esterno (è attivo se chiamo il metodo main)
     * 
     * @param gestore deve implementare Commandable
     * @throws CommandException
     */
    public void main(T gestore) throws CommandException {
        if(gestore == null) throw new CommandException("Errore, il gestore è null!");
        this.gestoreAttuale = gestore;
        this.attivo = true;
        this.factory = new CommandFactoryI<T> (gestore,manager);
        String menu = "";
        //TODO fare la stessa cosa della commandFactory usando hashmap e levare lo switchcase
        if(gestore instanceof GestoreClientServer) System.out.println("Terminale attivato \n\n--- Vista generale ---");
        if(gestore instanceof Server){
            System.out.print("--- Vista Server ---");
            try {
                new CommandStampaMsgRicevuti((Server)gestore).execute();
            } catch (CommandException e) {
                System.out.println(e.getMessage());
            } catch (ErrorLogException e) {
                this.errorLog(e.getMessage(),true);
            }
        }
        if(gestore instanceof Client) System.out.print("--- Vista Client ---\n");
        do{
            menu = "";
            
            if(!this.isBloccato()){
                
                if(gestore instanceof GestoreClientServer) System.out.print(">");
                if(gestore instanceof Server) System.out.print("(server) >");
                if(gestore instanceof Client){ System.out.print("(client) >");}
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
                    
                    if(gestore instanceof GestoreClientServer){
                        String conferma="";
                        do{
                            System.out.print("sicuro di voler chiudere il programma? [y/n] : ");
                            conferma = input.nextLine();
                            if(conferma.equals("y"))System.out.println("Chiusura Programma ...");
                            else if(conferma.equals("n"))menu = "";
                        }while(!conferma.equals("y") && !conferma.equals("n"));
                    } 
                    if(gestore instanceof Server) System.out.println("Chiusura vista Server ...\n" + "--- Vista generale ---");
                    if(gestore instanceof Client) System.out.println("Chiusura vista Client ...\n" + "--- Vista generale ---");
                    break;
                    
                default:
                    try{
                        this.executeCommand(factory.getCommand(menu.toLowerCase()));
                    }catch(CommandException e){
                        System.out.println(e.getMessage());
                    }catch(ErrorLogException e){
                        this.errorLog(e.getMessage(),true);
                    }
                    break;
                }
            }else{
                //se era bloccato allora sleep 5ms per verificare che si sblocchi
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    this.errorLog(e.getMessage(), true);
                }
            }
        }while(!menu.equalsIgnoreCase("quit"));
        this.factory = null;
        this.attivo = false;
    }

    /**permette di loggare un errore dall esterno del terminale
     * 
     * @param msg messaggio da loggare
     * @param video true se si stampa anche a video altrimenti false  solo su file
     */
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

    /**restituisce true se il terminale è attivo e sta usando il gestore che ha richiesto isAttivo altrimenti false
     * 
     * @param gestore gestore di cui si vuole sapere se è attivo il terminale 
     * @return restituisce true se il terminale è attivo e sta usando il gestore che ha richiesto isAttivo altrimenti false
     */
    public boolean isAttivo(T gestore){
        if(this.gestoreAttuale == null || gestore == null) return false;
        if(this.gestoreAttuale.equals(gestore))return this.attivo;
        return false;
    }
    public boolean isBloccato(){return this.bloccato;}
    public void setBloccato(boolean bloccato){this.bloccato = bloccato;}
    public CommandListManager getManager() {
        return manager;
    }

    public void modTelecomando(Client calling){
        if(this.telecomandi == null)
            this.telecomandi = new GestoreRemote();
        try {
            this.telecomandi.modTelecomando(calling);
        } catch (CommandException | ErrorLogException e) {
            this.errorLog(e.getMessage(), true);
        }
    }
}
