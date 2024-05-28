package it.davincifascetti.controllosocketudp.program;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandListManager;

public interface Component {
    public abstract CommandListManager getManager();
    public abstract void setManager(CommandListManager manager) throws CommandException;
    public abstract void setUi(Ui ui) throws CommandException;
}
