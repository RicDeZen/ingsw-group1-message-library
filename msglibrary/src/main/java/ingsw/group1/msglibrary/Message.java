package ingsw.group1.msglibrary;

import androidx.annotation.Nullable;

/**
 * @author Riccardo De Zen based on decisions of whole class.
 * @param <D> The type of data this message contains.
 * @param <P> The type of {@code Peer} this message allows to communicate.
 */
public abstract class Message<D, P extends Peer>{
    /**
     * @return the data in this message
     */
    public abstract D getData();

    /**
     * @return the peer for this message
     */
    public abstract P getPeer();

    /**
     * Method to check whether this message is valid
     * @return true if the message is valid, false if not
     */
    public abstract boolean isValid();

    /**
     * By default a message, as defined here, does not equal another, because two messages with same
     * Data and Peer can exist.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
