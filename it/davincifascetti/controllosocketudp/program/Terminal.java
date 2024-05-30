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
import it.davincifascetti.controllosocketudp.program.user.User;
import it.davincifascetti.controllosocketudp.program.user.UserDefault;

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
    private static AtomicBoolean  bloccato = new AtomicBoolean(); //rende boolean thread safe
    //componenti
    //?passargli l'errorLog e renderlo concorrente?
    //TODO capire se farlo o no
    private GestoreRemote telecomandi = null;
    private Cli cli = null;
    private Video video = null;
    private GestoreRisposte gestoreRisposte = null; //gestisce risposte server in base ai comandi di user
    
    /**
     * 
     * @param errorLog oggetto error log che si occupera del log su file degli errori
     * @throws CommandException
     */
    //TODO capire come gestire USER, per ora rappresenta solo i comandi di CLI
    public Terminal(ErrorLog errorLog,GestoreClientServer business,User comandi) throws CommandException{
        super(business, errorLog,comandi);
        this.telecomandi = new GestoreRemote(this); //si registra a gestoreClientServer (per rimuovere il client)
        this.cli = new Cli(this.getUser().getManager(),this); 
        this.video = new Video(this); //lui si regristra per ricevere risposte video da uno specifico server, toglie la registrazione di gestoreRisposte a quel server
        this.gestoreRisposte = new GestoreRisposte(this.getUser().getManager(),this); //lui si registrerà per ricevere risposte di server

        this.cli.setVista(this.getBusiness());
    }

    /**peremtte di avviare il terminale, se bloccato è true gli input sono bloccati, se attivo allora attivo = true altrimenti false , serve a capire se il terminale è attivo dall'esterno (è attivo se chiamo il metodo main)
     * 
     * @param gestore deve implementare Commandable
     * @throws CommandException
     */
    @Override
    public void main() throws CommandException {
        
        System.out.println("Terminale attivato");
        this.cli.main(Terminal.input);
        
    }

    
    public boolean isAttivo(Commandable gestore){
        return this.cli.isAttivo(gestore);
    }


    
    

    public synchronized static boolean isBloccato(){return Terminal.bloccato.get();}
    public synchronized static void setBloccato(boolean bloccato){Terminal.bloccato.set(bloccato);}
    


    public GestoreRemote getGestoreRemote(){
        return this.telecomandi;
    }
    public GestoreRisposte getGestoreRisposte(){
        return this.gestoreRisposte;
    }
    public Cli getCli(){return this.cli;}
    public Video getVideo(){return this.video;}
    
    @Override
    protected void init() {

        //TODO devo registrare la CLI agli eventi di Server e Client
        //TODO devo registrare il Video ad uno specifico Server 
        this.subscribeVideo();
        this.subscribeCli();
        this.subscribeGestoreRemote();
        this.subscribeGestoreRisposte();
    }

    private void subscribeVideo(){
        //business.newServer("servervideo")
        //nuovoServer.getEventManager().subscribe(this.getVideo())
    }
    private void subscribeCli(){
        this.getBusiness().getEventManager().subscribe(GestoreClientServer.CLIENT_RIMOSSO, this.getCli());
        this.getBusiness().getEventManager().subscribe(GestoreClientServer.CLIENT_AGGIUNTO, this.getCli());

        this.getBusiness().getEventManager().subscribe(GestoreClientServer.SERVER_RIMOSSO, this.getCli());
        this.getBusiness().getEventManager().subscribe(GestoreClientServer.SERVER_AGGIUNTO, this.getCli());
        
        this.getBusiness().getEventManager().subscribe(Client.SERVER_NO_RESPONSE, this.getCli());
        this.getBusiness().getEventManager().subscribe(Client.MESSAGGIO_INVIATO, this.getCli());
    }
    private void subscribeGestoreRemote(){
        this.getBusiness().getEventManager().subscribe(GestoreClientServer.CLIENT_RIMOSSO, this.getGestoreRemote());
    }
    private void subscribeGestoreRisposte(){
        this.getBusiness().getEventManager().subscribe(GestoreClientServer.SERVER_RIMOSSO, this.getGestoreRisposte());
    }



    


    
    
}
