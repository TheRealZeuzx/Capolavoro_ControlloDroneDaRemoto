package it.davincifascetti.controllosocketudp;

import it.davincifascetti.controllosocketudp.program.App;
import it.davincifascetti.controllosocketudp.program.user.UserDefault;

public class Main {
//!MAIN
    public static void main(String[] args) {
        /* msg utili per testing
        * new c c1 localhost 1212
        * new s s1 1212
        * select c c1
        */
        
        try {
            // User u;
            // u = new UserDrone("errorLog.txt");
            // System.out.println("\u001B[33m" + "UserDrone creato correttamente" + "\u001B[37m");
            // u.start();
            
            // User u2;
            // u2 = new UserDefault("errorLog.txt");
            // System.out.println("\u001B[33m" + "User creato correttamente" + "\u001B[37m");
            // u2.start();
            App app = new App("errorLog.txt",new UserDefault());
            System.out.println("\u001B[33m" + "User creato correttamente" + "\u001B[37m");
            app.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Programma Terminato");
       
    }
}
