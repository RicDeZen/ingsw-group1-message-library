package ingsw.group1.msglibrary.parsing;

import ingsw.group1.msglibrary.Message;

/**
 * Interface defining standard behaviour for a class aiming to manage sent and incoming {@code
 * Message} parsing.
 *
 * @param <M> The type of {@code Message} the Parser parses.
 * @author Riccardo De Zen
 */
public interface MessageParser<M extends Message> {
    /**
     * @param message The outgoing {@code Message}.
     * @return The formatted {@code Message}, ready to be sent.
     */
    M parseOutgoingMessage(M message);

    /**
     * @param message The incoming {@code Message}.
     * @return The parsed {@code Message}, as it would be before a
     * {@link MessageParser#parseOutgoingMessage(Message)} call.
     */
    M parseIncomingMessage(M message);
}
