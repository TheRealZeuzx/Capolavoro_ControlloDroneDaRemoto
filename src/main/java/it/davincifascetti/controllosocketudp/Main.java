package it.davincifascetti.controllosocketudp;

import it.davincifascetti.controllosocketudp.command.CommandListManager;
import it.davincifascetti.controllosocketudp.program.App;
import it.davincifascetti.controllosocketudp.program.user.UserDefault;
import it.davincifascetti.controllosocketudp.program.user.UserDrone;

public class Main {
//!MAIN
    public static void main(String[] args) {
        /* msg utili per testing
        * new c c1 localhost 1212
        * new s s1 1212
        * select c c1
        */
        try {
            App app = new App("errorLog.txt",new UserDefault().getUser());
            System.out.println("\u001B[33m" + "User creato correttamente" + "\u001B[37m");
            app.start();
        } catch (Exception e) {
            System.out.println("Programma Terminato a causa di questo errore: " + e.getMessage());
            for (StackTraceElement elem : e.getStackTrace()) {
                System.out.println(elem.toString());
            }
        }
       
    }
}
