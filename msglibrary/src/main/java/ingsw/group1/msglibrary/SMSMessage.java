package ingsw.group1.msglibrary;

import android.telephony.SmsMessage;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import ingsw.group1.msglibrary.exceptions.InvalidMessageException;


/**
 * Class implementing Message to represent an SMS-type message.
 * @author Riccardo De Zen based on decisions of whole class.
 */
@Entity(tableName = SMSMessage.SMS_TABLE_NAME)
public class SMSMessage implements Message<SMSPeer, String>{

    //Name of the Entity table inside the Database.
    public static final String SMS_TABLE_NAME = "smsmessage";
    public static final String SMS_ID_COLUMN_NAME = "id";
    public static final String SMS_ADDRESS_COLUMN_NAME = "address";
    public static final String SMS_BODY_COLUMN_NAME = "message";

    private static final String CON_ERROR =
            "The given message is invalid, refer to SMSMessage.isMessageValid(String address)";

    public static final int MAX_MESSAGE_LENGTH = 160;

    //The id is currently only relevant inside the database and does not need to be seen or set outside
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name = SMS_ID_COLUMN_NAME)
    private int id;
    @ColumnInfo(name = SMS_ADDRESS_COLUMN_NAME)
    private SMSPeer peer;
    @ColumnInfo(name = SMS_BODY_COLUMN_NAME)
    private String data;

    /**
     * @param peer the Peer associated with this Message
     * @param data the data to be contained in the message
     * @throws InvalidMessageException if the data for the message is not valid
     */
    @Ignore
    public SMSMessage(SMSPeer peer, String data){
        if(peer.isValid() && isMessageValid(data) != MessageValidity.MESSAGE_VALID)
            throw new InvalidMessageException(CON_ERROR);
        this.peer = peer;
        this.data = data;
    }

    /**
     * Constructor from a valid SmsMessage
     * @param message the message to be converted
     */
    @Ignore
    public SMSMessage(SmsMessage message){
        this(
                new SMSPeer(message.getOriginatingAddress()),
                message.getMessageBody()
        );
    }

    /**
     * Constructor to be used together with the database
     */
    public SMSMessage(int id, SMSPeer peer, String data){
        this(peer, data);
        this.id = id;
    }

    /**
     * Getter for the id
     * @return the id for this Message
     */
    public int getId(){
        return id;
    }

    /**
     * @return the data of this message
     */
    @Override
    public String getData() {
        return data;
    }

    /**
     * @return the peer of this message
     */
    @Override
    public SMSPeer getPeer() {
        return peer;
    }

    /**
     * @return true if this message is not empty and has a valid peer
     * This method should always return true
     */
    public boolean isValid(){
        return peer.isValid() && isMessageValid(data) == MessageValidity.MESSAGE_VALID;
    }

    /**
     * @param message the message whose validity should be checked
     * @return An enum value to indicate what is wrong with the message or that nothing is wrong
     */
    public static MessageValidity isMessageValid(String message){
        if(message == null || message.isEmpty())
            return MessageValidity.MESSAGE_EMPTY;

        if(message.length() > MAX_MESSAGE_LENGTH)
            return MessageValidity.MESSAGE_TOO_LONG;

        return MessageValidity.MESSAGE_VALID;
    }

    /**
     * @return the String type representation of this object
     */
    @NonNull
    public String toString(){
        return "Id: "+id+" =: Peer: "+peer.toString()+"; Data: "+data;
    }

    /**
     * Enum with values aimed to describe message validity
     */
    public enum MessageValidity{
        MESSAGE_TOO_LONG,
        MESSAGE_EMPTY,
        MESSAGE_VALID
    }
}
