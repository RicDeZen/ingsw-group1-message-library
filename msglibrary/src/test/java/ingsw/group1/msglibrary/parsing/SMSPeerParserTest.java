package ingsw.group1.msglibrary.parsing;

import org.junit.Assert;
import org.junit.Test;

import ingsw.group1.msglibrary.RandomSMSPeerGenerator;
import ingsw.group1.msglibrary.SMSPeer;
import ingsw.group1.msglibrary.exceptions.InvalidAddressException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link SMSPeerParser}. Tests assume positive results for
 * {@link ingsw.group1.msglibrary.RandomSMSPeerGeneratorTest}.
 *
 * @author Riccardo De Zen
 */
public class SMSPeerParserTest {
    private SMSPeerParser parser = new SMSPeerParser();

    @Test
    public void peerToDataAcceptsValid() {
        String validAddress = new RandomSMSPeerGenerator().generateValidAddress();
        SMSPeer peer = new SMSPeer(validAddress);
        assertEquals(validAddress, parser.peerToData(peer));
    }

    @Test
    public void peerToDataReturnsNullForInvalid() {
        Assert.assertNull(parser.peerToData(SMSPeer.INVALID_SMS_PEER));
    }

    //TODO test should run on all enum values
    @Test(expected = InvalidAddressException.class)
    public void dataToPeerThrowsForInvalidAddress(){
        parser.dataToPeer(new RandomSMSPeerGenerator().generateInvalidAddress());
    }

    @Test
    public void dataToPeerAcceptsValidAddress(){
        SMSPeer peer = new RandomSMSPeerGenerator().generateValidPeer();
        String peerData = parser.peerToData(peer);
        assertNotNull(peerData);
        assertEquals(peer, parser.dataToPeer(peerData));
    }
}
