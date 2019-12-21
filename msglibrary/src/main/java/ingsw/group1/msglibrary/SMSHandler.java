package ingsw.group1.msglibrary;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ingsw.group1.msglibrary.database.SMSDatabaseManager;

/**
 * Class meant to be the main body of the library. Handles sending and receiving SMS messages, also
 * handles notifying of sending and delivery confirms. Currently supports multi-instancing but doesn't
 * support ownership, should therefore be used through SMSManager which is singleton and partially
 * handles ownership.
 * @author Riccardo De Zen.
 */
public class SMSHandler {

    public static final String APP_KEY = "<#>";
    public static final String WAKE_KEY = "<urgent>";

    static final String RECEIVED_BROADCAST = "SMS_HANDLER_NEW_SMS";
    static final String SENT_BROADCAST = "SMS_HANDLER_SMS_SENT";
    static final String DELIVERED_BROADCAST = "SMS_HANDLER_SMS_DELIVERED";

    public static final String UNREAD_SMS_DATABASE_NAME = "sms-database";
    static final String PREFERENCES_FILE_NAME = "smshandler.PREFERENCES_FILE_NAME";
    static final String PREFERENCE_WAKE_ACTION_KEY = "smshandler.WAKE_ACTION";

    private static final String EXTRA_ADDRESS_KEY = "address";
    private static final String EXTRA_MESSAGE_KEY = "message";
    private static final int PI_DEFAULT_REQUEST_CODE = 0;

    private static final String[] ERRORS = {
            "A ReceivedMessageListener is already attached to this instance.",
            "A SentMessageListener is already attached to this instance.",
            "A DeliveredMessageListener is already attached to this instance."
    };
    //Contains references to all listeners to incoming sms.
    private static List<ReceivedMessageListener> activeReceivedListeners = new ArrayList<>();

    private String scAddress;

    private Context currentContext;
    private SmsManager smsManager;
    private ReceivedMessageListener<SMSMessage> receivedListener;
    private SentMessageListener<SMSMessage> sentListener;
    private DeliveredMessageListener<SMSMessage> deliveredListener;
    private SmsEventReceiver smsEventReceiver;
    /**
     * SmsManager.getDefault() can behave unpredictably if called from a
     * background thread in multi-SIM systems.
     * The user is responsible for providing and handling a valid context.
     */
    public SMSHandler(Context context){
        currentContext = context;
        smsManager = SmsManager.getDefault();
        smsEventReceiver = new SmsEventReceiver();
        registerReceiver();
    }

    /**
     * Method to be called only when the context that instantiated the object ceases to be valid,
     * this instance becomes invalid as well and will throw an exception when used.
     * The user is responsible for creating a new instance of this class with a new valid context.
     */
    public void onContextDestroyed(){
        currentContext.unregisterReceiver(smsEventReceiver);
        currentContext = null;
    }

    /**
     * The SmsEventReceiver class handling all three the main events is intentional
     * in order to reduce system resource consumption from having three distinct BroadcastReceivers
     */
    private class SmsEventReceiver extends BroadcastReceiver{
        /**
         * Default method for BroadcastReceivers. Verifies that there are incoming, sent or delivered text messages and
         * forwards them to a smsEventListener, if available.
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction() != null){
                if(intent.getAction().equals(RECEIVED_BROADCAST)) {
                    if (receivedListener != null){
                        for(SmsMessage message : Telephony.Sms.Intents.getMessagesFromIntent(intent)){
                            if(SmsUtils.isMessagePertinent(message))
                                receivedListener.onMessageReceived(new SMSMessage(message));
                        }
                    }
                }
                if(intent.getAction().equals(SENT_BROADCAST)){
                    SMSMessage m = new SMSMessage(
                            new SMSPeer(intent.getStringExtra(EXTRA_ADDRESS_KEY)),
                            intent.getStringExtra(EXTRA_MESSAGE_KEY)
                    );
                    if(sentListener != null) sentListener.onMessageSent(getResultCode(),m);
                }
                if(intent.getAction().equals(DELIVERED_BROADCAST)){
                    SMSMessage m = new SMSMessage(
                            new SMSPeer(intent.getStringExtra(EXTRA_ADDRESS_KEY)),
                            intent.getStringExtra(EXTRA_MESSAGE_KEY)
                    );
                    if(deliveredListener != null) deliveredListener.onMessageDelivered(getResultCode(),m);
                }
            }
        }
    }

    /**
     * Method to register the receiver when the object is instantiated
     */
    private void registerReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(RECEIVED_BROADCAST);
        filter.addAction(SENT_BROADCAST);
        filter.addAction(DELIVERED_BROADCAST);
        currentContext.registerReceiver(smsEventReceiver,filter);
    }

    /**
     * Method that sends a text message through SmsManager
     * @param destination the valid destination address for the message, in phone number format
     * @param message the valid body of the message to be sent
     * @param urgent whether this message should contain the code to fire a broadcast
     */
    public void sendSMS(String destination, @NonNull String message, boolean urgent){
        if(!SmsUtils.isMessageValid(message,urgent)) return;
        String body = SmsUtils.composeMessage(message,urgent);
        PendingIntent sentIntent;
        PendingIntent deliveryIntent;
        if(sentListener != null){
            Intent intent = new Intent(SENT_BROADCAST)
                    .putExtra(EXTRA_ADDRESS_KEY,destination)
                    .putExtra(EXTRA_MESSAGE_KEY,message);
            sentIntent = getPertinentPendingIntent(intent);
        }
        else sentIntent = null;
        if(deliveredListener != null){
            Intent intent = new Intent(DELIVERED_BROADCAST)
                    .putExtra(EXTRA_ADDRESS_KEY,destination)
                    .putExtra(EXTRA_MESSAGE_KEY,message);
            deliveryIntent = getPertinentPendingIntent(intent);
        }
        else deliveryIntent = null;
        smsManager.sendTextMessage(destination,scAddress,body,sentIntent,deliveryIntent);
    }

    /**
     * Method to return an appropriate PendingIntent
     * @param intent the base intent
     * @return the PendingIntent in charge of firing the given intent as a Broadcast
     */
    private PendingIntent getPertinentPendingIntent(Intent intent){
        return PendingIntent.getBroadcast(
                currentContext,
                generateRequestCode(),intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
    }

    /**
     * Method to generate a unique request code based on current system time
     * @return an int value ranging from 0 to Integer.MAX_VALUE
     */
    private int generateRequestCode(){
        return (int)(System.currentTimeMillis()%Integer.MAX_VALUE);
    }

    /**
     * Method to set the ReceivedMessageListener for this instance. Listener needs to be cleared before a new one is set.
     * @param newReceivedListener non-null new ReceivedMessageListener.
     * @throws IllegalStateException if a listener is already attached to this instance.
     */
    public void setReceivedListener(@NonNull ReceivedMessageListener<SMSMessage> newReceivedListener) throws IllegalStateException{
        if(receivedListener != null) throw new IllegalStateException(ERRORS[0]);
        receivedListener = newReceivedListener;
        activeReceivedListeners.add(receivedListener);
    }

    /**
     * Method to set the SentMessageListener for this instance. Listener needs to be cleared before a new one is set.
     * @param newSentListener non-null new SentMessageListener.
     * @throws IllegalStateException if a listener is already attached to this instance.
     */
    public void setSentListener(@NonNull SentMessageListener<SMSMessage> newSentListener) throws IllegalStateException{
        if(sentListener != null) throw new IllegalStateException(ERRORS[1]);
        sentListener = newSentListener;
    }

    /**
     * Method to set the ReceivedMessageListener for this instance. Listener needs to be cleared before a new one is set.
     * @param newDeliveredListener non-null new ReceivedMessageListener.
     * @throws IllegalStateException if a listener is already attached to this instance.
     */
    public void setDeliveredListener(@NonNull DeliveredMessageListener<SMSMessage> newDeliveredListener) throws IllegalStateException{
        if(deliveredListener != null) throw new IllegalStateException(ERRORS[2]);
        deliveredListener = newDeliveredListener;
    }

    /**
     * Method to clear this instance's attached ReceivedMessageListener. Albeit not necessary, a smsEventListener should
     * only try to unregister itself.
     */
    public void clearReceivedListener(){
        if(receivedListener == null) return;
        activeReceivedListeners.remove(receivedListener);
        receivedListener = null;
    }

    /**
     * Method to clear this instance's attached SentMessageListener. Albeit not necessary, a smsEventListener should
     * only try to unregister itself.
     */
    public void clearSentListener(){
        if(sentListener == null) return;
        sentListener = null;
    }

    /**
     * Method to clear this instance's attached DeliveredMessageListener. Albeit not necessary, a smsEventListener should
     * only try to unregister itself.
     */
    public void clearDeliveredListener(){
        if(deliveredListener == null) return;
        deliveredListener = null;
    }

    /**
     * Method to communicate whether at least one smsEventListener is attached to an instance of this class
     * whose BroadcastReceiver is listening for incoming Sms, and is thus requiring to be notified.
     * @return true if the activeReceivedListeners list is not empty.
     */
    static boolean shouldHandleIncomingSms(){ return !activeReceivedListeners.isEmpty();}

    /**
     * Method to load the unread sms messages and forward them to the listener asynchronously.
     * @return true if the listener is assigned and an attempt has been made, false otherwise.
     */
    public boolean loadUnread(){
        if(receivedListener != null){
            SMSDatabaseManager manager = SMSDatabaseManager.getInstance(currentContext);
            manager.forwardAllSMS(receivedListener);
            return true;
        }
        else return false;
    }

    /**
     * Method to save String name for the Activity that should wake up on urgent messages.
     * @param activityClass the Activity that should wake up.
     * @throws IllegalArgumentException if the passed class does not extend Activity.
     * @return true if the value was set, false otherwise.
     */
    public boolean setActivityToWake(Class activityClass){
        if(!Activity.class.isAssignableFrom(activityClass))
            throw new IllegalArgumentException("This method requires a class extending Activity");;
        String activityClassName = activityClass.getCanonicalName();
        SharedPreferences sharedPreferences = currentContext.getSharedPreferences(
                PREFERENCES_FILE_NAME,
                Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = sharedPreferences.edit()
                .putString(PREFERENCE_WAKE_ACTION_KEY, activityClassName);
        return editor.commit();
    }
}
