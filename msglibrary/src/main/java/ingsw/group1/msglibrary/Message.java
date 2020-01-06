package ingsw.group1.msglibrary;

/**
 * @author Riccardo De Zen based on decisions of whole class.
 * @param <P> The type of {@code Peer} this message allows to communicate.
 * @param <D> The type of data this message carries.
 */
public interface Message<P extends Peer, D>{
    /**
     * @return the peer for this message
     */
    P getPeer();

    /**
     * @return the data in this message
     */
    D getData();

    /**
     * Method to check whether this message is valid
     * @return true if the message is valid, false if not
     */
    boolean isValid();
}
