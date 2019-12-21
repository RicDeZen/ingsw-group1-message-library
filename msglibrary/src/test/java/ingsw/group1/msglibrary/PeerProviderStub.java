package ingsw.group1.msglibrary;

public interface PeerProviderStub<P extends Peer>{
    /**
     * @return a randomly generated valid Peer
     */
    P getRandomPeer();
}
