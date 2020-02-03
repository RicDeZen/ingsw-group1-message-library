package ingsw.group1.msglibrary.parsing;

import ingsw.group1.msglibrary.Peer;

/**
 * Interface defining standard behaviour for a class aiming to convert between a type of data
 * ({@code D}) and a type of Peer ({@code P}).
 *
 * @param <P> The type of Peer this Parser parses.
 * @param <D> The type of data this Parser uses.
 * @author Riccardo De Zen
 */
public interface PeerParser<P extends Peer, D> {
    /**
     * @param peer The {@code Peer} to convert into data.
     * @return The converted {@code Peer}.
     */
    D peerToData(P peer);

    /**
     * @param data The data to be parsed.
     * @return The {@code Peer} obtained from the data.
     */
    P dataToPeer(D data);
}