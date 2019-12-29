package ingsw.group1.msglibrary.exceptions;

/**
 * Exception meant to be thrown when an attempt to parse an unparsable {@code Message} is made.
 *
 * @author Riccardo De Zen
 */
public class UnparsableMessageException extends IllegalArgumentException {
    /**
     * Default constructor. Calls {@link IllegalArgumentException#IllegalArgumentException()}.
     */
    public UnparsableMessageException(){
        super();
    }

    /**
     * Constructor calling {@link IllegalArgumentException#IllegalArgumentException(String)}.
     * @param message The message for this Exception.
     */
    public UnparsableMessageException(String message){
        super(message);
    }
}