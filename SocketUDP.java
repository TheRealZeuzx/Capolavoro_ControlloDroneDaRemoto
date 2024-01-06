public abstract class SocketUDP {
    //! valutare se tenerla oppure no
    protected String nome;
    public boolean setNome(String nome){
        //è valido solo se non ci sono spazi ed è possibile usare solo lettere e _ (deve esserci almeno una lettera e almeno 2 a 18 caratteri)
        if(nome.matches("^(?=.*[a-zA-Z])[a-zA-Z_]{2,18}$")){
            this.nome = nome;
            return true;
        }
        return false;
    }
}
