package ingsw.group1.msglibrary;

/**
 * @author Riccardo De Zen based on decisions of whole class.
 * @param <D> The type of data this message contains.
 * @param <P> The type of {@code Peer} this message allows to communicate.
 */
public interface Message<D, P extends Peer>{
    /**
     * @return the data in this message
     */
    D getData();

    /**
     * @return the peer for this message
     */
    P getPeer();

    /**
     * Method to check whether this message is valid
     * @return true if the message is valid, false if not
     */
    boolean isValid();
}
