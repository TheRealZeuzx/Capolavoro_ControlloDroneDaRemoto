package it.davincifascetti.controllosocketudp.main;
import it.davincifascetti.controllosocketudp.main.errorlog.*;

public class Application {
    private static Application app = null;
    private UI userInterface = null;
    private BusinessLogic logic = null;

    private Application(UI ui, ErrorLog errorLog){
        this.userInterface = ui;
        this.logic = new BusinessLogic(errorLog);
    }

    public static Application getInstance(UI ui, ErrorLog errorLog){
        if(app == null)
            Application.app = new Application(ui, errorLog);
        return Application.app;
    }
}
