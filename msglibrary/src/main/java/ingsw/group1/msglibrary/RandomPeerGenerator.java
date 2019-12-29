package ingsw.group1.msglibrary;

/**
 * @author Riccardo De Zen.
 * @param <P> the type of generated Peer.
 */
public interface RandomPeerGenerator<A extends Comparable<A>, P extends Peer<A>> {
    /**
     * @return a valid built Peer.
     */
    P generateValidPeer();

    /**
     * @return an example of valid address.
     */
    A generateValidAddress();

    /**
     * @return an example of invalid address.
     */
    A generateInvalidAddress();
}
