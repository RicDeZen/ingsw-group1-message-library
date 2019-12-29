package ingsw.group1.msglibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import ingsw.group1.msglibrary.exceptions.InvalidAddressException;

/**
 * @author Riccardo De Zen. Based on decisions of whole class.
 */
public class SMSPeer implements Peer<String, SMSPeer> {
    /**
     * Field representing an Invalid SMSPeer. To avoid propagation of Invalid Peers this should
     * not be used as a return statement outside mock tests.
     */
    public static final SMSPeer INVALID_SMS_PEER = new SMSPeer(Token.INVALIDITY_TOKEN);
    /**
     * Default region for Peers. Used only when the number is not in International formatting.
     */
    public static final String DEFAULT_REGION = "IT";

    private enum Token {
        INVALIDITY_TOKEN
    }

    private static final PhoneNumberUtil utils = PhoneNumberUtil.getInstance();
    private static final String CON_ERROR = "Failed to create an SMSPeer from given address. " +
            "Reason: %s\n";

    @NonNull
    private String address;

    /**
     * This private constructor is used to create an invalid SMSPeer, it takes a dummy param
     * Token so that it doesn't take the space for a default constructor
     *
     * @param t token that is only used to have a parameter for the constructor
     */
    private SMSPeer(Token t) {
        if (t.equals(Token.INVALIDITY_TOKEN))
            address = "null";
        else
            //Should never be thrown.
            throw new IllegalStateException();
    }

    /**
     * @param address the address for the SMSPeer
     * @throws InvalidAddressException if the given address is invalid
     */
    public SMSPeer(@NonNull String address) {
        PhoneNumberValidity validity = getAddressValidity(address);
        if (validity != PhoneNumberValidity.VALID)
            throw new InvalidAddressException(String.format(CON_ERROR, validity.getMessage()));
        this.address = address;
    }

    /**
     * @return the address of the peer
     */
    @NonNull
    @Override
    public String getAddress() {
        return address;
    }

    /**
     * This method should always return true. It can return false if the address was modified
     * illegally.
     *
     * @return true if address fulfills international phone address standards
     */
    @Override
    public boolean isValid() {
        return getAddressValidity(address) == PhoneNumberValidity.VALID;
    }

    /**
     * @param address the address whose validity should be checked.
     * @return An enum value to indicate the validity state of the address.
     */
    public static PhoneNumberValidity getAddressValidity(@NonNull String address) {
        try {
            Phonenumber.PhoneNumber number = utils.parse(address, DEFAULT_REGION);
            if (utils.isPossibleNumber(number) && utils.isValidNumber(number))
                return PhoneNumberValidity.VALID;
            else
                return PhoneNumberValidity.NOT_VALID;
        } catch (NumberParseException e) {
            return PhoneNumberValidity.NOT_A_NUMBER;
        }
    }

    /**
     * @return String type representation of the Object
     */
    @NonNull
    public String toString() {
        return address;
    }

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
    public int compareTo(@NonNull SMSPeer peer) {
        return peer.getAddress().compareTo(this.getAddress());
    }

    /**
     * Enum to indicate the validity or invalidity of an address.
     */
    public enum PhoneNumberValidity {

        NOT_A_NUMBER("Address is not a phone number."),
        NOT_VALID("Phone number is invalid."),
        VALID("Phone number is valid.");

        private String message;

        PhoneNumberValidity(String message) {
            this.message = message;
        }

        /**
         * @return {@link PhoneNumberValidity#message}.
         */
        public String getMessage() {
            return message;
        }
    }
}
