import java.util.Scanner;

public class Terminal {
    private String[] history = new String[100];
    
    public static void main(String[] args) {
        
        String menu;
        Scanner input = new Scanner(System.in);
        
        menu = input.nextLine();
        switch(menu){
        
        case "1":
            //TODO creo una socket (input)
            break;

        case "2":
            //TODO mi collego ad socket remoto (input)
            break;
        
        case "3":
            //TODO  legge da tastiera un valore e usa per inviare dei dati ad un socket remoto
            break;

        default:
            break;
        }

    }

}
