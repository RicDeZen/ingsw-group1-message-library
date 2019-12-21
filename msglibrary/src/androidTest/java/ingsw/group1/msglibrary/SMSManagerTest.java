package ingsw.group1.msglibrary;

import android.content.Context;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ingsw.group1.msglibrary.exceptions.InvalidAddressException;
import ingsw.group1.msglibrary.exceptions.InvalidMessageException;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * @author Giorgia Bortoletti
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class SMSManagerTest {

    private SMSManager smsManager;
    private static final String EX_VALID_ADDRESS = "+39892424";

    @Before
    public void createSmsManager(){
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        this.smsManager = SMSManager.getInstance(context);
    }

    @Test
    public void sendMessage_bodyEmpty() {
        boolean sending = true;
        try {
            SMSPeer peer = new SMSPeer(EX_VALID_ADDRESS);
            String data = "";
            SMSMessage smsMessage = new SMSMessage(peer, data);
            this.smsManager.sendMessage(smsMessage);
        }catch(InvalidMessageException e){
            sending = false;
        }
        assertFalse(sending);
    }

    @Test
    public void sendMessage_peerEmpty() {
        boolean sending = true;
        try{
            SMSPeer peer = new SMSPeer("");
            String data = "body";
            SMSMessage smsMessage = new SMSMessage(peer, data);
            this.smsManager.sendMessage(smsMessage);
        }catch(InvalidAddressException e){
            sending = false;
        }
        assertFalse(sending);
    }

    @Test
    public void sendMessage_peerBlank() {
        boolean sending = true;
        try{
            SMSPeer peer = new SMSPeer("  ");
            String data = "body";
            SMSMessage smsMessage = new SMSMessage(peer, data);
            this.smsManager.sendMessage(smsMessage);
        }catch(InvalidAddressException e){
            sending = false;
        }
        assertFalse(sending);
    }

    /**
     * Peer not valid as global phone number
     */
    @Test
    public void sendMessage_peerNotValid() {
        boolean sending = true;
        try{
            SMSPeer peer = new SMSPeer("{{@@");
            String data = "body";
            SMSMessage smsMessage = new SMSMessage(peer, data);
            this.smsManager.sendMessage(smsMessage);
        }catch(InvalidAddressException e){
            sending = false;
        }
        assertFalse(sending);
    }

    @Test
    public void sendMessage_valid() {
        SMSPeer peer = new SMSPeer(EX_VALID_ADDRESS);
        String data = "body";
        SMSMessage smsMessage = new SMSMessage(peer, data);
        //TODO? not sendMessage because it does not have SEND_SMS permission
        assertTrue(this.smsManager.sendMessage(smsMessage));
    }
}
