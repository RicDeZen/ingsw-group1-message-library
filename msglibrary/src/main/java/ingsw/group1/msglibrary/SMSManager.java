package ingsw.group1.msglibrary;

import android.app.Activity;
import android.content.Context;

/**
 * Class to implement CommunicationHandler through use of SMS messages.
 * @author Riccardo De Zen based on decisions of whole class. Model proposed by Marco Cognolato and Luca Crema.
 */
public class SMSManager extends CommunicationHandler<SMSMessage>{

    private static SMSManager instance;

    private static Context currentContext;

    private SMSHandler smsHandler;

    private SMSManager(){
        //prevent use of reflection to change constructor to public during runtime
        if (instance != null)
            throw new RuntimeException("This class uses the singleton design pattern. Use getInstance() to get a reference to the single instance of this class");
        smsHandler = new SMSHandler(currentContext);
    }

    /**
     * Method to get the only valid instance of this class. A new instance is created only if it was
     * null previously. The used context is always the parent application context of the parameter.
     * @param context The calling context.
     * @return the SMSManager instance.
     */
    public static SMSManager getInstance(Context context){
        if(instance == null){
            SMSManager.currentContext = context.getApplicationContext();
            instance = new SMSManager();
        }
        return instance;
    }

    /**
     * Method that should be called when currentContext stops being valid or instance stops being used to
     * avoid a memory leak. The instance is made null so that calling getInstance again will provide
     * a new valid instance.
     * @param context the owning currentContext dropping the ownership.
     */
    public static void onContextDestroyed(Context context){
        if(context.getApplicationContext().equals(SMSManager.currentContext)){
            instance.removeReceiveListener();
            instance.smsHandler.onContextDestroyed();
            instance = null;
            SMSManager.currentContext = null;
        }
    }

    /**
     * method to send an SMSMessage to its associated Peer
     * @param message the valid SMSMessage to send
     * @return true if the message is valid and it has been sent, false otherwise
     */
    @Override
    public boolean sendMessage(SMSMessage message) {
        if(message.isValid()) {
            smsHandler.sendSMS(message.getPeer().getAddress(), message.getData(), false);
            return true;
        }
        return false;
    }

    /**
     * Method to send an SMSMessage classified as urgent (containing the corresponding code)
     * @param message the valid SMSMessage to send
     * @return true if the message is valid and it has been sent, false otherwise
     */
    public boolean sendUrgentMessage(SMSMessage message) {
        if(message.isValid()) {
            smsHandler.sendSMS(message.getPeer().getAddress(), message.getData(), true);
            return true;
        }
        return false;
    }

    /**
     * Setter for receivedListener in SMSHandler
     * @param newReceivedListener the new listener
     */
    @Override
    public void setReceiveListener(ReceivedMessageListener<SMSMessage> newReceivedListener) {
        removeReceiveListener();
        smsHandler.setReceivedListener(newReceivedListener);
    }

    @Override
    public void removeReceiveListener() {
        smsHandler.clearReceivedListener();
    }

    /**
     * Setter for sentListener in SMSHandler
     * @param newSentListener the new listener
     */
    public void setSentListener(SentMessageListener<SMSMessage> newSentListener) {
        removeSentListener();
        smsHandler.setSentListener(newSentListener);
    }

    /**
     * Method to remove sentListener
     */
    public void removeSentListener(){
        smsHandler.clearSentListener();
    }

    /**
     * Setter for deliveredListener in SMSHandler
     * @param newDeliveredListener the new listener
     */
    public void setDeliveredListener(DeliveredMessageListener<SMSMessage> newDeliveredListener) {
        removeDeliveredListener();
        smsHandler.setDeliveredListener(newDeliveredListener);
    }

    /**
     * Method to remove deliveredListener
     */
    public void removeDeliveredListener(){
        smsHandler.clearDeliveredListener();
    }

    /**
     * Method to load the unread sms messages and forward them to the listener asynchronously.
     * @return true if the listener is assigned and an attempt has been made, false otherwise.
     */
    public boolean loadUnread(){
        return smsHandler.loadUnread();
    }

    /**
     * Method to save String name for the Activity that should wake up on urgent messages.
     * @param activityClass the Activity Class that should wake up.
     * @throws IllegalArgumentException if the parameter class does not extend Activity
     * @return true if the value was set, false otherwise.
     */
    public boolean setActivityToWake(Class activityClass) throws IllegalArgumentException{
        if(Activity.class.isAssignableFrom(activityClass))
            return smsHandler.setActivityToWake(activityClass);
        else throw new IllegalArgumentException("This method requires a class extending Activity");
    }

    /**
     * Overload of setActivityToWake to allow passing an already existing object instead of its class.
     * The method surely avoids throwing an Exception.
     */
    public boolean setActivityToWake(Activity activity){
        return setActivityToWake(activity.getClass());
    }
}
