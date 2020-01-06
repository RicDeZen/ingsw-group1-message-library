package ingsw.group1.msglibrary.parsing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ingsw.group1.msglibrary.SMSPeer;
import ingsw.group1.msglibrary.exceptions.InvalidAddressException;

/**
 * Class defining an implementation of a {@link PeerParser} for
 * {@link ingsw.group1.msglibrary.SMSPeer}.
 *
 * @author Riccardo De Zen
 */
public class SMSPeerParser implements PeerParser<SMSPeer, String> {

    private static final String PARSE_ERR = "Data was not a valid address. Validity result: %s";

    /**
     * @param peer The valid {@code SMSPeer} to convert into data. Must not be {@code null}.
     * @return The converted {@code SMSPeer} in {@code String} format, returns null for an
     * invalid {@code peer}.
     */
    @Nullable
    public String peerToData(@NonNull SMSPeer peer) {
        if (peer.isValid())
            return peer.getAddress();
        return null;
    }

    /**
     * @param data The data to be parsed. A valid address is expected as data. Must not be {@code
     *             null}.
     * @return The {@code SMSPeer} obtained from the data.
     * @throws InvalidAddressException if {@code data} was not a valid address.
     */
    @NonNull
    public SMSPeer dataToPeer(@NonNull String data) throws InvalidAddressException {
        SMSPeer.PhoneNumberValidity validity = SMSPeer.getAddressValidity(data);
        if (validity != SMSPeer.PhoneNumberValidity.VALID)
            throw new InvalidAddressException(String.format(PARSE_ERR, validity));
        return new SMSPeer(data);
    }
}