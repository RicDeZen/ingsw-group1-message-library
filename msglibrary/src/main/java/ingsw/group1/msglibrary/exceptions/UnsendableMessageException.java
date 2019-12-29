package ingsw.group1.msglibrary.exceptions;

/**
 * Exception meant to be thrown when an attempt to send an unsendable {@code Message} is made.
 *
 * @author Riccardo De Zen
 */
public class UnsendableMessageException extends IllegalArgumentException {
    /**
     * Default constructor. Calls {@link IllegalArgumentException#IllegalArgumentException()}.
     */
    public UnsendableMessageException(){
        super();
    }

    /**
     * Constructor calling {@link IllegalArgumentException#IllegalArgumentException(String)}.
     * @param message The message for this Exception.
     */
    public UnsendableMessageException(String message){
        super(message);
    }
}
