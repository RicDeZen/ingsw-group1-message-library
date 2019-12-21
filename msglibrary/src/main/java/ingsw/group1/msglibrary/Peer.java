package ingsw.group1.msglibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Riccardo De Zen. Based on suggestion from Dr. Li Dao Hong.
 * @param <T> The type of address that identifies the Peer.
 */
public abstract class Peer<T extends Comparable<T>> implements Comparable<Peer<T>> {
    /**
     * @return the address of this Peer
     */
    public abstract T getAddress();

    /**
     * Method to check whether this Peer is valid
     * @return true if this Peer is valid, false if not
     */
    public abstract boolean isValid();

    /**
     * Two Peers are considered equal by default if their addresses are equal.
     *
     * @param obj the other Peer
     * @return true if the other Peer equals this, false if not.
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Peer) {
            Peer other = (Peer) obj;
            return other.getAddress().equals(this.getAddress());
        } else return false;
    }

    /**
     * Two Peers can, by default, be ordered using their address as a key.
     * @param peer the Peer to compare
     * @return the result of the comparison between the addresses
     */
    @Override
    public int compareTo(@NonNull Peer<T> peer) {
        return peer.getAddress().compareTo(this.getAddress());
    }
}
