package it.davincifascetti.controllosocketudp.command;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import it.davincifascetti.controllosocketudp.program.Server;

/**
    CommandFactoryClient.
    Factory per la creazione di comandi specifici per il server.
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
*/ 
//! la sto trasformando in una factory generale , utilizzando una HashMap, la HashMap non è synchronized
//!T è il gestore
//TODO delegare la separazione dei parametri ecc ad una classe apposita in modo da separarla dal comando che poi in base ai parametri agirà differentemente? oppure comandi diversi per parametri diversi?
//TODO quando registro il comando gli passo solo il gestore, all'esecuzione gli passero i parametri necessari.

//! la classe CommandFactoryRisposta va joinata a server
//! è singleton (devo registrarli una sola volta i comandi quindi è plausibile)(poi posso registrarli anche da fuori però creo una sola hashtable)?
public class CommandFactoryI<T extends Commandable>{

    private HashMap<String,String> arrayAssociativo = null;
    private T gestore = null; 
    /**
        Costruttore di default di CommandFactoryServer.
        @param gestore è l'oggetto che farà da receiver per i comandi 
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public CommandFactoryI(T gestore) throws CommandException{
        if(gestore == null) throw new CommandException("Errore, hai inserito un gestore null");
        this.gestore = gestore;
        this.arrayAssociativo = new HashMap<String,String>();
        
        //normali
        this.registraComando( "^\b(he?l?p?[ ]*)|[?][ ]*$","CommandHelp",false);
        this.registraComando( "^\bin?f?o?[ ]*$","CommandHelp",false);
        //set
        this.registraComando( "^\bse?t?[ ]+po?r?t?[ ]+.*$","CommandSetNomeServer",false);
        this.registraComando( "^\bse?t?[ ]+na?m?e?[ ]+.*$","CommandSetSocketServer",false);
        //$
        this.registraComando( "^\b\\$lo?g?[ ]*$","CommandStampaVideoServerThread",false);
        //default
        this.registraComando("default","CommandDefault",true);
        
    }

    /**
        getCommand.
        Metodo che, in base ai parametri, ritorna il comando corrispondente.
        @param params array di string contenente i parametri da cui instanziare i comandi corretti
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public Command getCommand(String params) throws CommandException {
        Vector<Object> arguments = new Vector<>();
        arguments.add(this.gestore);
        arguments.add(params);
        //TODO cambiare con un while
        //TODO capire come fare a dargli i parametri al comando (penso in esecuzione)
        for(Map.Entry<String, String> entry : this.arrayAssociativo.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Command temp = null;
            if(params.matches(key)){
                try {
                    temp = (Command)Class.forName(value).getConstructor().newInstance(arguments);
                } catch (Exception e){
                    throw new CommandException(e.getMessage());
                }
            }
            return temp;
        }
        return null;
        /* 
        String scelta = params == null || params.length == 0 ? "" : params[0];
        switch (scelta) {
            case "h":
            case "?":
            case "help":
                return new CommandHelp(          
                    "Comandi Terminale Server\n\n"+
                    "help\t\tpermette di visualizzare tutti i comandi \n" + 
                    "quit\t\tpermette di tornare al Terminale Generale \n" + 
                    "undo\t\tpermette di annullare l'ultima operazione significativa eseguita \n" + 
                    "info\t\tpermette di visualizzare le informazioni di questo server\n" +
                    "enable\t\tpermette di avviare questo server\n" +
                    "disable\t\tpermette di disattivare questo server\n" +
                    "set\t\tpermette di modificare la socket oppure il nome del server\n\t\t(set name nuovoNome) permette di cambiare il nome del server\n\t\t(set port nuovaPorta) permette di cambiare la porta del server\n"+
                    "file\t\tpermette di abilitare la stampa su file in maniera automatica di tutto ciò che viene inviato al server\n\t\t(file nomefile modalità) se si vuole stampare sul file che prende il nome di questo server , si usa 'this' al posto del nomeFile \n\t\tla modalità può essere append oppure overwrite\n\t\t(file disable) permette di disabilitare la stampa su file, una volta disabilitata,\n\t\tsarà necessario usare il comando (file nomefile modalita) per riattivarla\n"
                );
            case "info":
            case "i":
                return new CommandHelp(this.getGestore().toString());
            case "s":
            case "set":
                switch (params == null || params.length <= 2 ? "" : params[1]) {
                case "name":
                case "n":                      
                    if(params.length == 3)return new CommandSetNomeServer(this.getGestore(),params[2]);
                    throw new CommandException("Errore,non è stato specificato cosa creare");
                case "port":
                case "p":
                    if(params.length == 3)return new CommandSetSocketServer(this.getGestore(),params[2]);
                    throw new CommandException("Errore,non è stato specificato cosa creare");
                default:
                    throw new CommandException("Errore, non è stato specificato cosa selezionare");
                }
            case "en":
            case "enable": 
                return new CommandAttivaServer(this.getGestore());
            case "dis":
            case "disable": 
                return new CommandDisattivaServer(this.getGestore());
            case "file":
            case "f":
                if(params == null || params.length <= 1 || params.length > 3) throw new CommandException("Errore, non è stato specificato cosa selezionare");
                if(params.length == 2){
                    switch(params[1]){
                        case "dis":
                        case "disable":
                            return new CommandDisableToFile(this.getGestore());
                        default:  
                            throw new CommandException("Errore, non è stato specificato cosa selezionare");
                    }
                }
                switch(params[2]){
                    case "append":
                    case "a":
                        return new CommandEnableToFile(this.getGestore(),params[1],true);
                    case "overwrite":
                    case "o":
                        return new CommandEnableToFile(this.getGestore(),params[1],false);
                    default:  
                        throw new CommandException("Errore, non è stato specificato cosa selezionare");
                }
                
            default:    
                return new CommandDefault(params);
        }
        */
    }
    public void registraComando(String call,String CommandClass,boolean defualt) throws CommandException{

        if(call == null || CommandClass == null || call.isBlank() || CommandClass.isBlank()){
            try {
                if(Command.class.asSubclass(Class.forName(CommandClass)) != null)
                    if(defualt) this.arrayAssociativo.getOrDefault(call, CommandClass); else this.arrayAssociativo.put(call, CommandClass);
                else
                    throw new CommandException("La classe inserita implementa 'Command'");
            } catch (ClassNotFoundException e) {
                throw new CommandException(e.getMessage());
            }
        }else{
            throw new CommandException("Una delle stringhe inserite non è valida!");
        }

    }

    
}
