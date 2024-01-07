/**Classe astratta da cui estendere Server e Client per poter avere un padre comune, senza poter istanziarlo
 * 
 */
public abstract class SocketUDP {
    private String nome;
    private boolean stato;
    public boolean getStato(){return this.stato;}
    public void setStato(boolean stato){this.stato = stato;}
    public String getNome(){return this.nome;}
    public boolean setNome(String nome){
        //è valido solo se non ci sono spazi ed è possibile usare solo lettere e _ (deve esserci almeno una lettera e almeno 2 a 18 caratteri)
        if(nome.matches("^(?=.*[a-zA-Z])[a-zA-Z_]{2,18}$")){
            this.nome = nome;
            return true;
        }
        return false;
    }
   
}
