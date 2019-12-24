package ingsw.group1.msglibrary.exceptions;

/**
 * Message to be thrown when an invalid Geographical region is supplied during Peer creation.
 * @author Riccardo De Zen
 */
public class InvalidRegionException extends IllegalArgumentException {
    /**
     * Default Constructor
     */
    public InvalidRegionException(){
        super();
    }

    /**
     * @param message Message for this Exception
     */
    public InvalidRegionException(String message){
        super(message);
    }
}
