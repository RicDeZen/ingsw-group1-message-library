package ingsw.group1.msglibrary;

/**
 * @author Riccardo De Zen
 */
public class SMSMessageProviderStub implements MessageProviderStub<SMSPeer,SMSMessage> {

    private static final RandomSMSPeerGenerator GENERATOR = new RandomSMSPeerGenerator();
    /**
     * Generate a random Message and Peer
     *
     * @return the random Message
     */
    @Override
    public SMSMessage getRandomMessage() {
        SMSPeer peer = GENERATOR.generateValidPeer();
        String message = String.valueOf(peer.hashCode());
        return new SMSMessage(peer,message);
    }

    /**
     * Generate a random Message with an existing valid Peer as it's Peer
     *
     * @param peer the existing Peer of the same type required by the message
     * @return the random Message
     */
    @Override
    public SMSMessage getRandomMessage(SMSPeer peer) {
        String message = String.valueOf(peer.hashCode());
        return new SMSMessage(peer,message);
    }
}
