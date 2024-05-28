package it.davincifascetti.controllosocketudp.program;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

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
    
    public static Scanner input = new Scanner(System.in);
    private AtomicBoolean attivo = new AtomicBoolean();
    private static AtomicBoolean  bloccato = new AtomicBoolean(); //rende boolean thread safe
    private Commandable gestoreAttuale = null;

    //componenti
    //?passargli l'errorLog e renderlo concorrente?
    //TODO capire se farlo o no
    private  GestoreRemote telecomandi = null;
    private  Cli cli = null;
    private Video video = null;
    
    /**
     * 
     * @param errorLog oggetto error log che si occupera del log su file degli errori
     * @throws CommandException
     */
    public Terminal(ErrorLog errorLog,GestoreClientServer business) throws CommandException{
        super(business, errorLog);
        this.gestoreAttuale = business; 
        this.telecomandi = new GestoreRemote(this);
        this.cli = new Cli(new UserDefault().getManager(),this); //! per ora gli passo l'user a mano ma va cambiato
        this.video = new Video(this);
    }

    /**peremtte di avviare il terminale, se bloccato è true gli input sono bloccati, se attivo allora attivo = true altrimenti false , serve a capire se il terminale è attivo dall'esterno (è attivo se chiamo il metodo main)
     * 
     * @param gestore deve implementare Commandable
     * @throws CommandException
     */
    @Override
    public void main() throws CommandException {
        
        if(gestoreAttuale == null) throw new CommandException("Errore, il gestore ATTUALE è null!");


        //TODO fare la stessa cosa della commandFactory usando hashmap e levare lo switchcase
        if(gestoreAttuale instanceof GestoreClientServer) System.out.println("Terminale attivato \n\n--- Vista generale ---");
        if(gestoreAttuale instanceof Server){
            System.out.print("--- Vista Server ---");
            try {
                new CommandStampaMsgRicevuti((Server)gestoreAttuale).execute();
            } catch (CommandException e) {
                System.out.println(e.getMessage());
            } catch (ErrorLogException e) {
                this.errorLog(e.getMessage(),true);
            }
        }
        if(gestoreAttuale instanceof Client) System.out.print("--- Vista Client ---\n");


        this.attivo.compareAndSet(false, true);
        String menu = "";
        do{
            menu = "";

            if(gestoreAttuale instanceof GestoreClientServer) System.out.print(">");
            if(gestoreAttuale instanceof Server) System.out.print("(server) >");
            if(gestoreAttuale instanceof Client){ System.out.print("(client) >");}
            
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
                
                if(gestoreAttuale instanceof GestoreClientServer){
                    String conferma="";
                    do{
                        System.out.print("sicuro di voler chiudere il programma? [y/n] : ");
                        conferma = input.nextLine();
                        if(conferma.equals("y"))System.out.println("Chiusura Programma ...");
                        else if(conferma.equals("n"))menu = "";
                    }while(!conferma.equals("y") && !conferma.equals("n"));
                } 
                if(gestoreAttuale instanceof Server) System.out.println("Chiusura vista Server ...\n" + "--- Vista generale ---");
                if(gestoreAttuale instanceof Client) System.out.println("Chiusura vista Client ...\n" + "--- Vista generale ---");
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
            
        }while(!menu.equalsIgnoreCase("quit"));
        this.attivo.compareAndSet(true, false);
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
    

    public synchronized static boolean isBloccato(){return Terminal.bloccato.get();}
    public synchronized static void setBloccato(boolean bloccato){Terminal.bloccato.set(bloccato);}
    

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
