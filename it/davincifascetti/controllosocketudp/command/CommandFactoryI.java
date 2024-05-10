package it.davincifascetti.controllosocketudp.command;

import java.lang.reflect.InvocationTargetException;
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

    private Map<String,String> arrayAssociativo = null;
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
        //i comandi sono registrati dalla classe gestore
        gestore.registraComandi();
        this.comandoDefault = T.comandi.getCommandDefault();
        //usando Map.copyOf viene restituita una Map non modificabile
        this.arrayAssociativo = Map.copyOf(T.comandi.getComandi());
        if(this.arrayAssociativo == null) throw new CommandException("Errore, il gestore non ha comandi registrati");
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
                } catch (InvocationTargetException e){
                    throw new CommandException(e.getTargetException().getMessage());
                }catch (Exception e){
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

    }

 

}
