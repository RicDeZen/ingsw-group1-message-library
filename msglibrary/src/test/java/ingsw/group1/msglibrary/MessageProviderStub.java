package ingsw.group1.msglibrary;

public interface MessageProviderStub<P extends Peer, M extends Message<P,?>>{

    /**
     * Generate a random Message and Peer
     * @return the random Message
     */
    M getRandomMessage();

    /**
     * Generate a random Message with an existing valid Peer as it's Peer
     * @param peer the existing Peer of the same type required by the message
     * @return the random Message
     */
    M getRandomMessage(P peer);
}
