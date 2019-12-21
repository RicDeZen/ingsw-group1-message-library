package ingsw.group1.msglibrary.database;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.SmsMessage;

import androidx.annotation.NonNull;
import androidx.room.Room;

import java.util.List;

import ingsw.group1.msglibrary.ReceivedMessageListener;
import ingsw.group1.msglibrary.SMSMessage;

/**
 * Singleton class used to perform the two needed operations on the database containing unread sms.
 * This is not properly a singleton pattern because an on-disk instance and an in-memory instance can
 * coexist.
 * The reason for singleton design pattern is found here https://developer.android.com/training/data-storage/room
 *
 * @author Riccardo De Zen
 */
public class SMSDatabaseManager {

    private static final String CON_ERROR = "This class uses the singleton design pattern. Use getInstance() to get a reference to the single instance of this class";
    private static final String UNREAD_SMS_DB_NAME = "unread-sms-db";

    private static SMSDatabaseManager instance;
    //Having an in-memory instance is unnecessary. It is useful for testing and will not break anything.
    private static SMSDatabaseManager inMemoryInstance;
    private SMSDatabase database;

    /**
     * Constructor, handles instantiation of the database.
     */
    private SMSDatabaseManager(Context context, boolean inMemory) {
        //prevent use of reflection to change constructor to public at runtime
        if (inMemory) {
            if (instance != null)
                throw new RuntimeException(CON_ERROR);
            database = Room.databaseBuilder(context, SMSDatabase.class, UNREAD_SMS_DB_NAME)
                    .enableMultiInstanceInvalidation()
                    .allowMainThreadQueries()
                    .build();
        } else {
            if (inMemoryInstance != null)
                throw new RuntimeException(CON_ERROR);
            database = Room.inMemoryDatabaseBuilder(context, SMSDatabase.class)
                    .enableMultiInstanceInvalidation()
                    .allowMainThreadQueries()
                    .build();
        }
    }

    /**
     * Method to get the only valid on-disk instance of this class. A new instance is created only if it was
     * null previously. The used context is always the parent application context of the parameter.
     *
     * @param context The calling context.
     * @return the SMSDatabaseManager instance.
     */
    public static SMSDatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new SMSDatabaseManager(context, false);
        }
        return instance;
    }

    /**
     * Method to get the only valid on-memory instance of this class.
     *
     * @param context the calling context.
     * @return the SMSDatabaseManager instance.
     */
    public static SMSDatabaseManager getInMemoryInstance(Context context) {
        if (inMemoryInstance == null) {
            inMemoryInstance = new SMSDatabaseManager(context, true);
        }
        return inMemoryInstance;
    }

    /**
     * @return the number of stored SMS Messages
     */
    public int count(){
        return database.access().count();
    }

    /**
     * Method to add one or more SMS to the database, runs on the main Thread
     * @param newMessages the message/messages to be added
     */
    public void addSMS(SMSMessage... newMessages){
        SMSDao dbAccess = database.access();
        dbAccess.insert(newMessages);
    }

    /**
     * Method to add one or more SMS to the database, runs on the main Thread. Overload for SmsMessage.
     * @param newMessages the message/messages to be added
     */
    public void addSMS(SmsMessage... newMessages){
        if (newMessages == null || newMessages.length <= 0) return;
        SMSMessage[] messages = new SMSMessage[newMessages.length];
        for (int i = 0; i < newMessages.length; i++) {
            messages[i] = new SMSMessage(newMessages[i]);
        }
        addSMS(messages);
    }

    /**
     * Method to add SMS to the database. Starts an asynchronous task to do so.
     *
     * @param newMessages the message/messages to be added
     */
    public void addSMSAsync(SMSMessage... newMessages) {
        if (newMessages == null || newMessages.length <= 0) return;
        new AddSMSTask(database).execute(newMessages);
    }

    /**
     * Method to add SMS to the database. Starts an asynchronous task to do so. Overload for SmsMessage
     *
     * @param newMessages the message/messages to be added
     */
    public void addSMSAsync(SmsMessage... newMessages) {
        if (newMessages == null || newMessages.length <= 0) return;
        SMSMessage[] messages = new SMSMessage[newMessages.length];
        for (int i = 0; i < newMessages.length; i++) {
            messages[i] = new SMSMessage(newMessages[i]);
        }
        addSMSAsync(messages);
    }

    private static class AddSMSTask extends AsyncTask<SMSMessage, Integer, Boolean> {
        private SMSDatabase database;

        public AddSMSTask(SMSDatabase database) {
            this.database = database;
        }

        @Override
        protected Boolean doInBackground(SMSMessage... smsMessages) {
            try {
                SMSDao dbAccess = database.access();
                dbAccess.insert(smsMessages);
                return Boolean.TRUE;
            } catch (Exception e) {
                e.printStackTrace();
                return Boolean.FALSE;
            }
        }
    }

    /**
     * Method to send all unread SMS to the given listener, SMS are deleted once listener returns.
     * Starts an asynchronous task to perform the operations.
     *
     * @param listener the valid listener
     */
    public void forwardAllSMS(@NonNull ReceivedMessageListener<SMSMessage> listener) {
        new ForwardSMSTask(database, listener).execute();
    }

    private static class ForwardSMSTask extends AsyncTask<String, Integer, Boolean> {
        private SMSDatabase database;
        private ReceivedMessageListener<SMSMessage> listener;

        public ForwardSMSTask(SMSDatabase database, ReceivedMessageListener<SMSMessage> listener) {
            this.database = database;
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(String... args) {
            SMSDao dbAccess = database.access();
            try {
                List<SMSMessage> unreadMessages = dbAccess.getAll();
                for (SMSMessage message : unreadMessages) {
                    if (message.isValid()) listener.onMessageReceived(message);
                    dbAccess.delete(message);
                }
                return Boolean.TRUE;
            } catch (Exception e) {
                e.printStackTrace();
                return Boolean.FALSE;
            }
        }
    }
}
