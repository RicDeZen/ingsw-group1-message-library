package ingsw.group1.msglibrary.parsing;

import org.junit.Assert;
import org.junit.Test;

import ingsw.group1.msglibrary.RandomSMSPeerGenerator;
import ingsw.group1.msglibrary.SMSPeer;

import static org.junit.Assert.assertEquals;

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

    @Test(expected = IllegalArgumentException.class)
    public void dataToPeerThrowsForInvalidAddress() {
        parser.dataToPeer(new RandomSMSPeerGenerator().generateInvalidAddress());
    }

    @Test
    public void dataToPeerAcceptsValidAddress() {
        String peerData = new RandomSMSPeerGenerator().generateValidAddress();
        SMSPeer expected = new SMSPeer(peerData);
        assertEquals(expected, parser.dataToPeer(peerData));
    }
}
