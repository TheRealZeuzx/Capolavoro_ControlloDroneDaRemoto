package it.davincifascetti.controllosocketudp.command;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;
import it.davincifascetti.controllosocketudp.program.Ui;

/**
 * FACTORY
    CommandFactoryClient.
    Factory per la creazione di comandi specifici per il server.
    @author Tommaso Mussaldi
    @version 1.0
*/ 
//! in teoria ora funziona con tutte le classi che estendono Commandable (continua a distinguere i comandi per classi)
public class CommandFactoryI implements CommandFactory{

    private Map<String,String> arrayAssociativo = null;
    private String comandoDefault = null;
    private Commandable gestore = null; 
    private CommandListManager manager = null;
    /**
        Costruttore di default di CommandFactoryServer.
        
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public CommandFactoryI() throws CommandException{
        super();     
    }
    /**
        Costruttore di default di CommandFactoryServer.
        
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
    */
    public CommandFactoryI(Commandable gestore, CommandListManager manager) throws CommandException{
        this.setManager(manager);
        this.setGestore(gestore);
    }

    /**utilizza il gestore impostato precedentemente
     * 
     */
    public Command getCommand(String params, Ui ui) throws CommandException{
        return this.getCommand(this.gestore, params,ui);
    }

    /**
        getCommand. utilizza il gestore passato. (cambia il gestore attuale della factory)
        Metodo che, in base ai parametri, ritorna il comando corrispondente.Utilizza una hashmap per salvare i comandi che andranno registrati dall esterno della classe
        @param params stringa contenente i parametri da cui instanziare i comandi corretti
        @param gestore è l'oggetto che farà da receiver per i comandi 
        @throws CommandException Eccezione generale sollevata da tutti i comandi in caso di errore.
     * @throws ErrorLogException 
    */
    
    public Command getCommand(Commandable gestore,String params, Ui ui) throws CommandException{
        if(manager == null) throw new CommandException("Errore, prima devi impostare un manager!");
        this.setGestore(gestore);
        if(params == null) new CommandDefault("Parametri sono null!");
        if(ui == null) new CommandDefault("Ui è null!");


        Class<? extends Commandable> gestoreClass = gestore.getClass();
        //i comandi sono registrati dalla classe gestore
        this.comandoDefault = manager.getCommandList(gestoreClass).getCommandDefault();
        //usando Map.copyOf viene restituita una Map non modificabile
        this.arrayAssociativo = manager.getCommandList(gestoreClass).getComandi();
        if(this.arrayAssociativo == null) throw new CommandException("Errore, il gestore non ha comandi registrati");


        Vector<Object> arguments = new Vector<>();
        arguments.add(this.gestore);
        Command temp = null;
        //TODO cambiare con un while
        for(Map.Entry<String, String> entry : this.arrayAssociativo.entrySet()) {
            String key = entry.getKey(); // regex
            String value = entry.getValue(); // comando di riferimento
            String tempP = CommandFactoryI.controllaRegexGruppo(key, params);
            //controllo che non tempP non sia null, non sia empty non ci siano parole prima del match (caso errato: ciao new client c1 dove new client è il comando e c1 sarà il parametro)
            if(tempP != null && !tempP.isEmpty() && params.substring(0,tempP.length()).equals(tempP)){
                try {
                    arguments.add(params.substring(tempP.length(),params.length()));
                    arguments.add(ui);
                    if(Class.forName(value).getDeclaredConstructors()[0].getParameterTypes()[0].isInterface())//nel caso in cui sia generale
                        temp = (Command)Class.forName(value).getDeclaredConstructor(Commandable.class,String.class,Ui.class).newInstance(arguments.toArray());
                    else
                        temp = (Command)Class.forName(value).getDeclaredConstructor(this.gestore.getClass(),String.class,Ui.class).newInstance(arguments.toArray());
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
                arguments.add(ui);
                temp = (Command)Class.forName(this.comandoDefault).getDeclaredConstructor(this.gestore.getClass(),String.class,Ui.class).newInstance(arguments.toArray());
            }
        } catch (Exception e){
            throw new CommandException(e.getMessage());
        }
        return temp == null ? new CommandDefault(params) : temp;
        
    }

    /**restiuisce solo la parte che fa match con la regex (la prima se ce ne fossero più di una)
     * per creare la regex omettere ^ e $
     * @param regex
     * @param valore stringa su cui applicare la regex
     * @return la prima sottostringa che corrisponde alla regex se non ce ne sono restiuisce null
     * @throws CommandException
     */
    public static String controllaRegexGruppo(String regex,String valore) throws CommandException{
        if(valore == null) throw new CommandException("Errore, il valore è null!");
        Pattern pattern = null;
        try {
            pattern = Pattern.compile(regex); // compile della regex
        } catch (Exception e) {
            throw new CommandException("Errore, la regex non è valida!");
        }
        Matcher matcher = pattern.matcher(valore);  // match tra regex e parametro 
        return  matcher.find() ? matcher.group() : null; // find == risultati di matcher, group crea la substring per il risultato
    } 

    /**controlla che tutta la stringa sia verificata dalla regex 
     * per creare la regex usare ^ come primo carattere e $ come ultimo
     * @param regex
     * @param valore stringa su cui applicare la regex
     * @return true se tutta la stringa corrisponde alla regex altrimenti false
     * @throws CommandException
     */
    public static boolean controllaRegexAssoluta(String regex,String valore) throws CommandException{
        if(valore == null) throw new CommandException("Errore, il valore è null!");
        try {
            return Pattern.matches(regex,valore); // compile della regex
        } catch (Exception e) {
            throw new CommandException("Errore, la regex non è valida!");
        }
    } 

    public void setManager(CommandListManager manager) throws CommandException{
        if(manager == null) throw new CommandException("Errore, hai inserito un manager null");
        this.manager = manager;
    }
    public void setGestore(Commandable gestore) throws CommandException{
        if(gestore == null) throw new CommandException("Errore, hai inserito un gestore null");
        this.gestore = gestore;
    }

}
