package ingsw.group1.msglibrary.exceptions;

public class InvalidMessageException extends IllegalArgumentException {
    public InvalidMessageException(String message){
        super(message);
    }
}
