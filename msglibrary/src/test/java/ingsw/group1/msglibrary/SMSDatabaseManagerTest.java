package ingsw.group1.msglibrary;

import android.content.Context;
import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.Random;

import ingsw.group1.msglibrary.database.SMSDatabaseManager;

/**
 * @author Riccardo De Zen
 */
@Config(sdk = Build.VERSION_CODES.P)
@RunWith(RobolectricTestRunner.class)
public class SMSDatabaseManagerTest {

    private Context context;
    private MessageListener listener;
    private SMSDatabaseManager database;
    private SMSMessageProviderStub messageProvider;

    private static class MessageListener implements ReceivedMessageListener<SMSMessage> {
        private int receivedMessages = 0;
        @Override
        public void onMessageReceived(SMSMessage message) {
            receivedMessages++;
        }
    }

    @Before
    public void init(){
        context = ApplicationProvider.getApplicationContext();
        database = SMSDatabaseManager.getInMemoryInstance(context);
        listener = new MessageListener();
        messageProvider = new SMSMessageProviderStub();
    }

    @Test
    public void addSingleSMS(){
        int count = database.count();
        int expected = count + 1;
        database.addSMS(messageProvider.getRandomMessage());
        Assert.assertEquals(expected, database.count());
    }

    @Test
    public void addSingleSms(){

    }

    @Test
    public void addMultipleSMS(){
        int count = database.count();
        int expected = count + 2;
        database.addSMS(messageProvider.getRandomMessage(), messageProvider.getRandomMessage());
        Assert.assertEquals(expected, database.count());
    }

    @Test
    public void addMultipleSms(){

    }

    @Test
    public void forwardSMS(){
        int howMany = new Random().nextInt() % 100;
        for(int i = 0 ; i < howMany; i++) addSingleSMS();
        int count = database.count();
        database.forwardAllSMS(listener);
        Assert.assertEquals(count, listener.receivedMessages);
    }


    /**
     * Since the database is a singleton class, if the instance is not cleared between tests it can
     * give problems, and it's not done automatically by the testing environment. Reflection is used
     * to crack open the SMSDatabaseManager class and reset the instance.
     */
    @After
    public void resetInstance() {
        try{
            Field instance = SMSDatabaseManager.class.getDeclaredField("inMemoryInstance");
            instance.setAccessible(true);
            instance.set(null, null);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}