import it.davincifascetti.controllosocketudp.command.CommandException;
import it.davincifascetti.controllosocketudp.program.user.User;
import it.davincifascetti.controllosocketudp.program.user.UserDefault;

public class Main {
//!MAIN
    public static void main(String[] args) {
        /* msg utili per testing
        * new c c1 localhost 1212
        * new s s1 1212
        * select c c1
        */
        User u2;
        try {
            // u = new UserDrone("errorLog.txt");
            // System.out.println("\u001B[33m" + "UserDrone creato correttamente" + "\u001B[37m");
            // u.start();
            
            
            u2 = new UserDefault("errorLog.txt");
            System.out.println("\u001B[33m" + "User creato correttamente" + "\u001B[37m");
            u2.start();
        } catch (CommandException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Programma Terminato");
       
    }
}
