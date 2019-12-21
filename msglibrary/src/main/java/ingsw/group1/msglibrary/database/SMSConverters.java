package ingsw.group1.msglibrary.database;

import androidx.room.TypeConverter;

import ingsw.group1.msglibrary.SMSPeer;

class SMSConverters {
    /**
     * Static method defining the conversion between an SMSPeer and a String field that can be saved
     * in the database.
     *
     * @param peer the Peer to be converted.
     * @return the String representation for the Peer (currently the address).
     */
    @TypeConverter
    public static String fromSMSPeer(SMSPeer peer) {
        return peer.getAddress();
    }

    /**
     * Static method defining the conversion between a String saved in the database and an SMSPeer.
     * Since only a valid Peer can be created an thus stored in the Database, no exceptions should
     * ever be thrown.
     *
     * @param address the address for the Peer
     * @return The Peer created fro the String
     */
    @TypeConverter
    public static SMSPeer fromString(String address) {
        return new SMSPeer(address);
    }
}
