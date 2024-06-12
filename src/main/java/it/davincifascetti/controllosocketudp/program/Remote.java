package it.davincifascetti.controllosocketudp.program;
import javax.swing.JFrame;

import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.command.CommandFactoryRemote;
import it.davincifascetti.controllosocketudp.command.CommandInviaMsgClient;
import it.davincifascetti.controllosocketudp.command.CommandListManager;
import it.davincifascetti.controllosocketudp.errorlog.ErrorLogException;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Remote extends Component implements KeyListener{
    private Client client;
    private JFrame frame;
    private Character tastoPrecedente;
    private CommandFactoryRemote factory = null;
    
    public Remote(Ui ui,CommandListManager manager) throws CommandException{
        this.setUi(ui);
        this.setManager(manager);
        this.factory = new CommandFactoryRemote(this.getManager());
    }

    /**attiva la modalità telecomando (apre un JFrame per gli input e invia i msg al server)
     * 
     * @throws CommandException
     * @throws ErrorLogException
     */
    public void start(Client client) throws CommandException, ErrorLogException{
        if(this.getClient() != null) throw new CommandException("Errore, il telecomando è già attivo!");
        this.setClient(client);
        if(this.frame == null)this.frame = new JFrame("Premi per inviare");
        this.frame.requestFocus(); 
        this.frame.setSize(500, 500);
        this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.frame.addKeyListener(this); 
        this.frame.setVisible(true);
        this.frame.setAlwaysOnTop(true); 
        System.out.println(this.getManager().getCommandList(null).getStringaHelp());//recuperarlo dal manager
    }

    public Client stop(){
        if(this.frame != null){
            this.frame.removeKeyListener(this);
            this.frame.dispose();
        }
        this.client.setDesc(null);
        Client c = this.client;
        this.client = null;
        ((Terminal)this.getUi()).getCli().setLocked(false);
        return c;
    }

    @Override
    public void destroy() {
        super.destroy();
        this.factory = null;
        this.client = null;
        if(this.frame != null)this.frame.dispose();
        this.frame = null;
    }

    /**KeyEvent che viene chiamato all keyevent del jFrame
     * 
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // System.out.println(e.getKeyChar());
        try {
            this.executeCommand(this.getFactory().getCommand(this.getClient(),Integer.toString((int)e.getKeyChar()), this.getUi()),null);
        } catch (CommandException e1) {
            ((Terminal)this.getUi()).getCli().printError(e1.getMessage());
        } catch (ErrorLogException e1) {
            ((Terminal)this.getUi()).getCli().printError(e1.getMessage());
        }

    }
    
    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public boolean equals(Object o){
        if(o != null && this.getClient()!=null){
            if(Remote.class.isInstance(o))
                if(((Remote)o).getClient() != null)
                    if(((Remote)o).getClient().equals(this.getClient()))
                        return true;
        }
        return false;   
    }  

    private void setClient(Client client) throws CommandException {
        if(client == null) throw new CommandException("Errore, il client è null!");
        this.client = client;
    }

    public Client getClient() {
        return this.client; 
    }

    public CommandFactoryRemote getFactory(){
        return this.factory;
    }

    public boolean isActive(){
        return this.frame == null ? false : this.frame.isActive();
    }
}   