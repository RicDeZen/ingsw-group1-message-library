package ingsw.group1.msglibrary;

/**
 * @author Riccardo De Zen
 * @param <M> The type of Message received.
 */
public interface ReceivedMessageListener<M extends Message> {
    /**
     * Called when a message is received
     * @param message the received message that needs to be forwarded
     */
    void onMessageReceived(M message);
}
