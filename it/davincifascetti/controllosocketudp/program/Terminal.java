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
import it.davincifascetti.controllosocketudp.program.user.UserDefault;

//!nello schema fatto su figma, questa classe rappresenta Terminal e non Cli!
/**classe Terminal si occupa della gestione del terminale, utilizza i command per eseguire le operazioni richieste,
 * ha la possibilità di fare l'undo dei comandi che implementano UndoableCommand
 * instanzia una commandFactory corrispondente al gestore utilizzando CommandFactoryInstantiar 
 * la command factory si occupa di creare i comandi opportuni per ogni messaggio inviato dal utente, la execute del command , attivera il receiver che eseguira effettivaemnte l'operazione richiesta
 * utilizza il command design pattern
 * @author Mussaldi Tommaso, Mattia Bonfiglio
 * @version 1.0
 */
public class Terminal extends Ui{
    
    private CommandFactoryI<? extends Commandable> factory;//!
    public static Scanner input = new Scanner(System.in);
    private boolean attivo = false;
    private static boolean bloccato = false;
    private Commandable gestoreAttuale = null; //!

    //componenti
    //?passargli l'errorLog e renderlo concorrente?
    //TODO capire se farlo o no
    private  GestoreRemote telecomandi = new GestoreRemote();
    private  Cli cli = new Cli(new UserDefault().getManager());
    private Video video = new Video(null);
    
    /**
     * 
     * @param errorLog oggetto error log che si occupera del log su file degli errori
     * @throws CommandException
     */
    public Terminal(ErrorLog errorLog,GestoreClientServer business) throws CommandException{
        super(business, errorLog);
        this.gestoreAttuale = business; 
    }

    /**peremtte di avviare il terminale, se bloccato è true gli input sono bloccati, se attivo allora attivo = true altrimenti false , serve a capire se il terminale è attivo dall'esterno (è attivo se chiamo il metodo main)
     * 
     * @param gestore deve implementare Commandable
     * @throws CommandException
     */
    public void main() throws CommandException {
        if(Terminal.isBloccato()) return;
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
            
            if(!Terminal.isBloccato()){
                
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

    
    public boolean isAttivo(Commandable gestore){
        return this.gestoreAttuale.equals(gestore);
    }


    /**permette di loggare un errore dall esterno del terminale
     * 
     * @param msg messaggio da loggare
     * @param video true se si stampa anche a video altrimenti false  solo su file
     */
    protected void errorLog(String msg,boolean video){
        if(video)System.out.println(msg);
        new ErrorLogCommand(this.getErrorLog(),msg).execute();
    }
    

    public synchronized static boolean isBloccato(){return Terminal.bloccato;}
    public synchronized static void setBloccato(boolean bloccato){Terminal.bloccato = bloccato;}
    

    public void modTelecomando(Client calling) throws CommandException, ErrorLogException{
        if(calling == null) throw new CommandException("Errore, il client calling è null!");
        telecomandi.modTelecomando(calling);
    }

    public GestoreRemote getGestoreRemote(){
        return this.telecomandi;
    }

    public Cli getCli(){return this.cli;}
    public Video getVideo(){return this.video;}
    
    @Override
    protected void init() {
        this.getBusiness().getEventManager().subscribe(GestoreClientServer.CLIENT_RIMOSSO, this.getGestoreRemote());
        this.getBusiness().getEventManager().subscribe(this.getCli());
        this.getBusiness().getEventManager().subscribe(this.getVideo());
    }

}
