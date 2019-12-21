package ingsw.group1.msglibrary.database;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import ingsw.group1.msglibrary.SMSMessage;
import ingsw.group1.msglibrary.SMSPeer;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Giorgia Bortoletti
 */
@RunWith(AndroidJUnit4.class)
public class SMSDatabaseTest {

    private SMSDatabase smsDatabase;
    private String bodyMessage = "text";
    private String addressPeer = "+39892424";
    private SMSPeer peer;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        smsDatabase = Room.inMemoryDatabaseBuilder(context, SMSDatabase.class).build();
        peer = new SMSPeer(addressPeer);
    }

    @After
    public void closeDb() throws IOException {
        smsDatabase.close();
    }

    /**
     * Test for insert SMSMessage which is created from constructor WITHOUT specifying parameter id
     */
    @Test
    public void insert_withoutMessageIdSpecified() {
        SMSMessage message = new SMSMessage(peer, bodyMessage);
        SMSDao dbAccess = smsDatabase.access();
        int countBeforeInsert = dbAccess.count();
        dbAccess.insert(message);
        int countAfterInsert = countBeforeInsert+1;
        assertEquals(countAfterInsert , dbAccess.count());
    }

    /**
     * Test for insert SMSMessage which is created from constructor WITH parameter id specified
     */
    @Test
    public void insert_withMessageIdSpecified() {
        SMSMessage message = new SMSMessage(4, peer, bodyMessage);
        SMSDao dbAccess = smsDatabase.access();
        int countBeforeInsert = dbAccess.count();
        dbAccess.insert(message);
        int countAfterInsert = countBeforeInsert+1;
        assertEquals(countAfterInsert , dbAccess.count());
    }

    /**
     * Test for delete SMSMessage which is created from constructor WITHOUT specifying parameter id
     */
    @Test
    public void delete_withoutMessageIdSpecified() {
        SMSMessage message = new SMSMessage(peer, bodyMessage);
        SMSDao dbAccess = smsDatabase.access();
        dbAccess.insert(message);
        int countBeforeDelete = dbAccess.count();
        List<SMSMessage> messages = dbAccess.getAll();
        dbAccess.delete(messages.get(messages.size()-1));
        int countAfterDelete = countBeforeDelete-1;
        assertEquals(countAfterDelete, dbAccess.count());
    }

    /**
     * Test for delete SMSMessage which is created from constructor WITH parameter id specified
     */
    @Test
    public void delete_withMessageIdSpecified() {
        SMSMessage message = new SMSMessage(5, peer, bodyMessage);
        SMSDao dbAccess = smsDatabase.access();
        dbAccess.insert(message);
        int countBeforeDelete = dbAccess.count();
        dbAccess.delete(message);
        int countAfterDelete = countBeforeDelete-1;
        assertEquals(countAfterDelete, dbAccess.count());
    }
}

