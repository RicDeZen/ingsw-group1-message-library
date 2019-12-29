package ingsw.group1.msglibrary;

/**
 * @author Riccardo De Zen. Based on suggestion from Dr. Li Dao Hong.
 * @param <T> The type of address that identifies the Peer.
 */
public interface Peer<T extends Comparable<T>, P extends Peer<T,P>> extends Comparable<P> {
    /**
     * @return the address of this Peer
     */
    T getAddress();

    /**
     * Method to check whether this Peer is valid
     * @return true if this Peer is valid, false if not
     */
    boolean isValid();
}
