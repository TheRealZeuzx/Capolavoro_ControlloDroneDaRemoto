import java.io.File;


/**@
 */
public class Error {
    File log;
    //Si occupa di scrivere l'eccezione su file
    // file url 
    public Error(String fileName){
        this.log = new File(fileName);
    }

    public boolean logToFile(String errorMessage){
        try{
            openStream();
            appendToStream(errorMessage);
            closeStream();
            return true;
        }catch(Exception e){
            return false;
        }
    }

    private boolean openStream(){
        
        return true;
    }

    private boolean appendToStream(String message){
        
        return true;
    }

    private boolean closeStream(){
        
        return true;
    }


}
