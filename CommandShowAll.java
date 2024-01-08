/**permette di stampare la lista di client e server presenti nel GestoreClientServer
 * 
 */
public class CommandShowAll extends CommandI<GestoreClientServer>{

    public CommandShowAll(GestoreClientServer gestore) throws CommandException {
        super(gestore);
    }

    public void execute() throws CommandException{
        if(!this.getGestore().stampaLista(true,true)) throw new CommandException("");
    }

}
