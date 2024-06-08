package it.davincifascetti.controllosocketudp.program;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.Commandable;
import it.davincifascetti.controllosocketudp.command.CommandableException;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLog;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.user.User;

/**La classe Terminal si occupa di gestire i propri componenti e eseguire azioni in base a 
 * gli eventi e i msg ricevuti dalla parte business dell'applicazione , in questo caso GestoreClientServer.
 * in questo caso il terminale istanzia la CLI che sarà il vero e proprio terminale ovvero dove avverrano input e output, sarà il terminale stesso
 * a decidere chi e come userà la CLI per l'output, farà da mediator (Mediator Design Pattern)
 */
public class Terminal extends Ui {
    
    public static Scanner input = new Scanner(System.in);
    private GestoreRemote telecomandi = null;
    private Cli cli = null;
    private Video video = null;
    private GestoreRisposte gestoreRisposte = null;
    
    /**
     * 
     * @param errorLog oggetto error log che si occupera del log su file degli errori
     * @throws CommandException
     * @throws IOException 
     */
    //TODO capire come gestire USER, per ora rappresenta solo i comandi di CLI
    public Terminal(ErrorLog errorLog,GestoreClientServer business,User comandi) throws CommandException, IOException{
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
     * @throws IOException 
     */
    @Override
    public void main() throws CommandException, IOException {
        //!funny
        try {
            //https://manytools.org/hacker-tools/ascii-banner/ 
            //https://patorjk.com/software/taag/#p=testall&f=Graffiti&t=CAPOLAVORO
            //https://www.asciiart.eu/computers/computers
            //per cambiarlo devi metterlo nel file
            FileReader fileTesto = new FileReader("./art/art6.txt");
            Scanner in = new Scanner(fileTesto);  
            System.out.print(Cli.BANNER_COLOR);
            while(in.hasNextLine()) {
                System.out.println(in.nextLine());
            }
            System.out.println("\nType '"+Cli.INPUT_COLOR+"help"+Cli.BANNER_COLOR+"' or '"+Cli.INPUT_COLOR+"?"+Cli.BANNER_COLOR + "' to see list of available commands" + Cli.RESET_COLOR +"\n");
            in.close();
            fileTesto.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        this.cli.setScanner(input);
        this.cli.main();
        
        
    }

    //TODO rimuoverlo da qui
    public boolean isAttivo(Commandable gestore){
        return this.cli.isAttivo(gestore);
    }

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
        this.getBusiness().getEventManagerClient().subscribe(this);

        
    }


    //TODO quando elimino un client o server devo rimuoverlo dalle liste componenti esempio elimino un client, devo rimuoverlo dalla lista di gestoreRemote
    @Override
    public void update(String eventType, Commandable commandable) {
        if(commandable == null){this.getCli().printError("Errore!");} //!gestire
        this.getCli().print(commandable.getClass().getSimpleName() + " dice che è successo questo: " + eventType); 
        //if(eventType.equals(Client.MESSAGE_SENT)) this.getCli().setLocked(true); 
        
        //TODO capire se conviene mettere un attributo bloccatoVideo in CLI in modo che anche se è bloccata la CLI possa stampare ad esempio un msg di risposta
        //if(eventType.equals(Client.MESSAGE_RECEIVED) || eventType.equals(Client.SERVER_NO_RESPONSE)) this.getCli().setLocked(false); 
        
    }

    @Override
    public void update(byte[] buffer, int lung, Commandable commandable) {
        if(commandable == null){this.getCli().printError("Errore, qualcosa è andato storto!");} //!gestire   
        //messaggio ricevuto in dal server da un client (ricevuto dal server)
        if(ServerThread.class.isInstance(commandable)){
            //in base alla descrizione decido come gestire es: getDesc.equals("video") --> aggiorno il video
            if(commandable.getDesc() == null){
                try {
                    this.getGestoreRisposte().gestisciRisposta(buffer, lung, (ServerThread)commandable);
                } catch (CommandException e) {
                    this.getCli().printError(e.getMessage());
                } catch (ErrorLogException e) {
                    this.getCli().printError(e.getMessage());
                    this.fileErrorLog(e.getMessage());
                }
            }else if(commandable.getDesc().equals("video")){
                this.getVideo().updateVideo(buffer, lung); //!quando andrò a creare il server che riceve il video, assegnerò come desc: video =/
            }
        }
        //messaggio ricevuto in risposta ad una richiesta del client (ricevuto dal client)
        if(Client.class.isInstance(commandable)){
            if(commandable.getDesc() == null){
                try {
                    this.getGestoreRisposte().gestisciRisposta(buffer, lung, (Client)commandable);
                } catch (CommandException e) {
                    this.getCli().printError(e.getMessage());
                } catch (ErrorLogException e) {
                    this.getCli().printError(e.getMessage());
                    this.fileErrorLog(e.getMessage());
                }
            }
        }
    }

    @Override
    public void fileErrorLog(String msg){
        try {
            this.getErrorLog().log(msg);
        } catch (IOException e) {
            this.getCli().printError(e.getMessage());
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
