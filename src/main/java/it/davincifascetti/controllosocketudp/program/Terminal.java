package it.davincifascetti.controllosocketudp.program;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLog;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.user.User;

/**classe Terminal si occupa della gestione del terminale, utilizza i command per eseguire le operazioni richieste,
 * ha la possibilità di fare l'undo dei comandi che implementano UndoableCommand
 * instanzia una commandFactory corrispondente al gestore utilizzando CommandFactoryInstantiar 
 * la command factory si occupa di creare i comandi opportuni per ogni messaggio inviato dal utente, la execute del command , attivera il receiver che eseguira effettivaemnte l'operazione richiesta
 * utilizza il command design pattern
 * @author Mussaldi Tommaso, Mattia Bonfiglio
 * @version 1.0
 */
public class Terminal extends Ui implements EventListenerRicezioneBuffer,EventListenerCommandable{
    
    public static Scanner input = new Scanner(System.in);
    private static AtomicBoolean  bloccato = new AtomicBoolean(); //rende boolean thread safe
    //componenti
    //?passargli l'errorLog e renderlo concorrente?
    //TODO capire se farlo o no
    private GestoreRemote telecomandi = null;
    private Cli cli = null;
    private Video video = null;
    private GestoreRisposte gestoreRisposte = null;
    
    /**
     * 
     * @param errorLog oggetto error log che si occupera del log su file degli errori
     * @throws CommandException
     */
    //TODO capire come gestire USER, per ora rappresenta solo i comandi di CLI
    public Terminal(ErrorLog errorLog,GestoreClientServer business,User comandi) throws CommandException{
        super(business, errorLog,comandi);
        this.telecomandi = new GestoreRemote(this);
        this.cli = new Cli(this.getUser().getManager(),this); //agirà di input output con l'utente
        this.video = new Video(this); 
        this.gestoreRisposte = new GestoreRisposte(this.getUser().getManager(),this);
        
        this.cli.setVista(this.getBusiness());
    }

    /**peremtte di avviare il terminale, se bloccato è true gli input sono bloccati, se attivo allora attivo = true altrimenti false , serve a capire se il terminale è attivo dall'esterno (è attivo se chiamo il metodo main)
     * 
     * @param gestore deve implementare Commandable
     * @throws CommandException
     */
    @Override
    public void main() throws CommandException {
        //!funny
        try {
            //https://manytools.org/hacker-tools/ascii-banner/ 
            //https://patorjk.com/software/taag/#p=testall&f=Graffiti&t=CAPOLAVORO
            //https://www.asciiart.eu/computers/computers
            //per cambiarlo devi metterlo nel file
            FileReader fileTesto = new FileReader("./art/art6.txt");
            Scanner in = new Scanner(fileTesto);  
            while(in.hasNextLine()) {
                System.out.println("\u001B[35m" + in.nextLine() + "\u001B[0m");
            }
            in.close();
            fileTesto.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
  
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

        //!la ui si registra, fa lo switch per i casi, controlla il server se è desc= null oppure video e poi chiama il component che esegue le op
        this.getBusiness().getEventManager().subscribe(EventManagerCommandable.SUBSCRIBE_ALL,this);
        this.getBusiness().getEventManagerClient().subscribe(EventManagerCommandable.SUBSCRIBE_ALL,this);
        this.getBusiness().getEventManagerServer().subscribe(EventManagerCommandable.SUBSCRIBE_ALL,this);
        this.getBusiness().getEventManagerServer().subscribe(this);

        
    }

    @Override
    public void update(String eventType, Commandable commandable) {
        if(commandable == null){System.out.println("Errore!");} //!gestire
        System.out.println("è successo questo: " + eventType);
    }

    @Override
    public void update(byte[] buffer, int lung, Commandable commandable) {
        if(commandable == null){System.out.println("Errore!");} //!gestire
        System.out.println("qualcuno ha detto: " + new String(buffer));
        if(ServerThread.class.isInstance(commandable)){
            //in base alla descrizione decido come gestire es: getDesc.equals("video") --> aggiorno il video
            if(commandable.getDesc() == null){
                try {
                    this.getGestoreRisposte().gestisciRisposta(buffer, lung, (ServerThread)commandable);
                } catch (CommandException e) {
                    //credo errore alla cli? o al errorLog
                    try {
                        this.getErrorLog().log(e.getMessage());
                    } catch (IOException e1) {
                        System.err.println(e.getMessage());
                    }
                }
            }else if(commandable.getDesc().equals("video")){
                this.getVideo().updateVideo(buffer, lung); //!quando andrò a creare il server che riceve il video, assegnerò come desc: video =/
            }
        }
    }

    // private void subscribeVideo(){
    //     business.newServer("servervideo")
    //     nuovoServer.getEventManager().subscribe(this.getVideo())
    // }
    // private void subscribeCli(){
    //     this.getBusiness().getEventManager().subscribe(GestoreClientServer.CLIENT_RIMOSSO, this.getCli());
    //     this.getBusiness().getEventManager().subscribe(GestoreClientServer.CLIENT_AGGIUNTO, this.getCli());

    //     this.getBusiness().getEventManager().subscribe(GestoreClientServer.SERVER_RIMOSSO, this.getCli());
    //     this.getBusiness().getEventManager().subscribe(GestoreClientServer.SERVER_AGGIUNTO, this.getCli());
        
    //     this.getBusiness().getEventManagerClient().subscribe(Client.SERVER_NO_RESPONSE, this.getCli());
    //     this.getBusiness().getEventManagerClient().subscribe(Client.MESSAGGIO_INVIATO, this.getCli());


    //     this.getBusiness().getEventManagerServer().subscribe(Server.ASCOLTO_INIZIATO, this.getCli());
    //     this.getBusiness().getEventManagerServer().subscribe(Server.ASCOLTO_TERMINATO, this.getCli());
    //     this.getBusiness().getEventManagerClient().subscribe(this.getCli());
    // }
    // private void subscribeGestoreRemote(){
    //     this.getBusiness().getEventManager().subscribe(GestoreClientServer.CLIENT_RIMOSSO, this.getGestoreRemote());
    // }
    // private void subscribeGestoreRisposte(){
    //     this.getBusiness().getEventManager().subscribe(GestoreClientServer.SERVER_RIMOSSO, this.getGestoreRisposte());
    //     this.getBusiness().getEventManagerServer().subscribe(this.getGestoreRisposte());
    // }



    


    
    
}
