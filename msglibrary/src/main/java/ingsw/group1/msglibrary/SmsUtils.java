package ingsw.group1.msglibrary;

import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class with experimental methods to help testing of some features.
 * @author Riccardo De Zen
 */
public class SmsUtils {
    //projection to use for SMS Inbox query
    private static String[] inboxProjection = {
            Telephony.Sms._ID,
            Telephony.Sms.ADDRESS,
            Telephony.Sms.SUBJECT,
            Telephony.Sms.BODY
    };

    /**
     * Method to get SMS Inbox representation. Used for testing and debugging.
     * @param context the calling context used to get the ContentResolver cursor.
     * @return a list containing the SMSMessages
     */
    public static List<SMSMessage> getInbox(@NonNull Context context){
        List<SMSMessage> list = new ArrayList<>();
        String[] selectionArgs = null;
        String selectionClause = null;
        Cursor mCursor = context.getContentResolver().query(
                Telephony.Sms.CONTENT_URI,
                inboxProjection, selectionClause, selectionArgs,
                Telephony.Sms._ID);
        if(mCursor != null && mCursor.getCount() >= 1){
            mCursor.moveToFirst();
            while(!mCursor.isLast()){
                SMSMessage sms = new SMSMessage(new SMSPeer(mCursor.getString(1)),mCursor.getString(3));
                Log.d("SMSUtils", sms.toString());
                mCursor.moveToNext();
            }
            mCursor.close();
        }
        return list;
    }

    /**
     * Method to get the sim's country code to be used when formatting addresses
     * @param context the calling context to get the TelephonyManager service.
     * @return a String containing the country code or null if not available.
     */
    public static String getCountryCode(@NonNull Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if(tm != null){
            return tm.getSimCountryIso();
        }
        return null;
    }

    /**
     * PhoneNumberUtils.formatNumber(...) requires capital letters country code
     * @param context the valid calling context to get the TelephonyManager service.
     * @return a String containing the upper case country code or null if not available.
     */
    public static String getCapitalCountryCode(@NonNull Context context){
        String countryCode = getCountryCode(context);
        if(countryCode != null) return countryCode.toUpperCase();
        return null;
    }

    /**
     * Adds the country phone prefix corresponding to the given country Code if not already present
     * @param number The valid phone number to be formatted
     * @param countryCode the countryCode in capital letters
     * @return The number with the added country code or the original number if the country code
     * cannot be applied (invalid number or country code)
     */
    public static String formatSMSNumber(String number, String countryCode){
        String formattedNumber = PhoneNumberUtils.formatNumberToE164(number,countryCode);
        return (formattedNumber != null) ? formattedNumber : number;
    }

    /**
     * Method to detect whether the given message is ok to be sent through the library
     * @param message the main body of the message
     * @param urgent whether it should include SMSHandler.WAKE_KEY
     */
    static boolean isMessageValid(String message, boolean urgent){
        String body = SMSHandler.APP_KEY + message + (urgent ? SMSHandler.WAKE_KEY : "");
        return body.length() <= 160;
    }

    /**
     * Method to compose the body of a message that passed isMessageValid
     */
    static String composeMessage(String message, boolean urgent){
        return SMSHandler.APP_KEY + message + (urgent ? SMSHandler.WAKE_KEY : "");
    }

    /**
     * Methods to tell whether the message is pertinent to the app or not
     */
    static boolean isMessagePertinent(String body){
        String messageHead = body.substring(0,3);
        return messageHead.equals(SMSHandler.APP_KEY);
    }
    static boolean isMessagePertinent(SMSMessage message){
        String messageBody = message.getData();
        String messageHead = messageBody.substring(0,3);
        return messageHead.equals(SMSHandler.APP_KEY);
    }
    static boolean isMessagePertinent(SmsMessage message){
        String messageBody = message.getMessageBody();
        String messageHead = messageBody.substring(0,3);
        return messageHead.equals(SMSHandler.APP_KEY);
    }
}
