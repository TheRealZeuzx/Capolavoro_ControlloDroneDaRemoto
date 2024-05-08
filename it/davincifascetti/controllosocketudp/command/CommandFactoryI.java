package it.davincifascetti.controllosocketudp.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
    CommandFactoryClient.
    Factory per la creazione di comandi specifici per il server.
    @author Tommaso Mussaldi, Mattia Bonfiglio
    @version 1.0
*/ 
//! la sto trasformando in una factory generale , utilizzando una HashMap, la HashMap non è synchronized
//!T è il gestore
//TODO delegare la separazione dei parametri ecc ad una classe apposita in modo da separarla dal comando che poi in base ai parametri agirà differentemente? oppure comandi diversi per parametri diversi?


//! la classe CommandFactoryRisposta va joinata a server
public class CommandFactoryI<T extends Commandable> implements CommandFactory{

    private HashMap<String,String> arrayAssociativo = null;
    private String comandoDefault = null;
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
        //i comandi sono registrati dalla classe gestore
        gestore.registraComandi((CommandFactory)this);
        
        
    }

    /**
        getCommand.
        Metodo che, in base ai parametri, ritorna il comando corrispondente.Utilizza una hashmap per salvare i comandi che andranno registrati dall esterno della classe
        @param params stringa contenente i parametri da cui instanziare i comandi corretti
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public Command getCommand(String params) throws CommandException {
        Vector<Object> arguments = new Vector<>();
        arguments.add(this.gestore);
        Command temp = null;
        //TODO cambiare con un while
        for(Map.Entry<String, String> entry : this.arrayAssociativo.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Pattern pattern = Pattern.compile(key);
            Matcher matcher = pattern.matcher(params);
            String tempP = matcher.find() ? matcher.group() : null;
            //controllo che non tempP non sia null, non sia empty non ci siano parole prima del match (caso errato: ciao new client c1 dove new client è il comando e c1 sarà il parametro)
            if(tempP != null && !tempP.isEmpty() && params.substring(0,tempP.length()).equals(tempP)){
                try {
                    arguments.add(params.substring(tempP.length(),params.length()));
                    temp = (Command)Class.forName(value).getDeclaredConstructor(this.gestore.getClass(),String.class).newInstance(arguments.toArray());
                } catch (Exception e){
                    throw new CommandException(e.getMessage());
                }
            }

        }
        //se ho impostato il comando di default lo istanzio altrimenti uso CommandDefault
        try {
            if(temp == null && this.comandoDefault != null){
                arguments.add(params);
                temp = (Command)Class.forName(this.comandoDefault).getDeclaredConstructor(this.gestore.getClass(),String.class).newInstance(arguments.toArray());
            }
        } catch (Exception e){
            throw new CommandException(e.getMessage());
        }
        return temp == null ? new CommandDefault(params) : temp;
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

    /**permette la registrazione di un comando, viene fatto dal gestore stesso che si occuperà di registrare tutti i comandi di cui necessita
     * @param call stringa con la quale viene identificato il comando (usare una regexp che identifichi la parte necessaria a richiamare il comando)
     * @param CommandClass la classe del comando deve extendere CommandI<T extends Commandable> (deve avere il costruttore con parametri Commandable,String)
     * @param default valore booleano che indica se il comando verrà impostato come comando di default (c'è un solo comando di default), nel caso in cui sia impostato a true, la stringa call verrà ignorata
     * inizialmente il comando di default è impostato su CommandDefault
     * @throws CommandException
     */
    //?nel caso in cui venga inserita una GUI , si può inserire un riferimento ad un bottone o simile come chiave idk
    //!IMPORTANTE, fare in modo che la registrazione dei comandi avvenga una sola volta da parte delle classi commandable (ci posso essere più istanze)
    public void registraComando(String call,String CommandClass,boolean defaultCommand) throws CommandException{
        if(((call == null && defaultCommand) || call != null) && CommandClass != null && !call.isBlank() && !CommandClass.isBlank()){
            try { System.out.println(CommandClass);
                if(CommandI.class.isAssignableFrom(Class.forName(CommandClass)))
                    if(!defaultCommand)this.arrayAssociativo.put(call, CommandClass); else this.comandoDefault = CommandClass;
                else
                    throw new CommandException("La classe inserita non implementa 'Command'");
            } catch (ClassNotFoundException e) {
                throw new CommandException("errore, la classe inserita non è stata trovata ('" + e.getMessage() + "')");
            }
        }else{
            throw new CommandException("Una delle stringhe inserite non è valida!");
        }

    }
    /**permette la registrazione di un comando, viene fatto dal gestore stesso che si occuperà di registrare tutti i comandi di cui necessita
     * @param call stringa con la quale viene identificato il comando (usare una regexp)
     * @param CommandClass la classe del comando deve extendere CommandI<T extends Commandable> (deve avere il costruttore con parametri Commandable,String)
     * @throws CommandException
     */
    public void registraComando(String call,String CommandClass) throws CommandException{
        this.registraComando(call,CommandClass,false);
    }

}
