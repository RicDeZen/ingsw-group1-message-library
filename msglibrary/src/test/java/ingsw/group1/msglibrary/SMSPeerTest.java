package ingsw.group1.msglibrary;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ingsw.group1.msglibrary.exceptions.InvalidAddressException;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Giorgia Bortoletti
 * @author Riccardo De Zen
 * This test needs a positive result in tests for RandomSMSPeerGenerator in order to work.
 */
@RunWith(Parameterized.class)
public class SMSPeerTest {

    private static final int TEST_RUNS = 1;
    private static final RandomSMSPeerGenerator GENERATOR = new RandomSMSPeerGenerator();

    private SMSPeer peer;

    /**
     * @return parameters for the test. List is empty because it's needed to simply run the test
     * multiple times.
     */
    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[TEST_RUNS][0];
    }

    @Test(expected = InvalidAddressException.class)
    public void constructorFails() {
        String invalidAddress = GENERATOR.generateInvalidAddress();
        peer = new SMSPeer(invalidAddress);
    }

    @Test
    public void constructorPasses() {
        peer = new SMSPeer(GENERATOR.generateValidAddress());
        assertNotNull(peer);
    }

    @Test
    public void getAddress() {
        String address = GENERATOR.generateValidAddress();
        peer = new SMSPeer(address);
        assertEquals(address, peer.getAddress());
    }

    @Test
    public void equalsSame() {
        String address = GENERATOR.generateValidAddress();
        peer = new SMSPeer(address);
        SMSPeer other = new SMSPeer(address);
        assertEquals(peer, peer);
        assertEquals(peer, other);
    }

    @Test
    public void notEqualsDifferent() {
        String firstAddress = GENERATOR.generateValidAddress();
        String secondAddress;
        //Just making sure the addresses are different.
        do {
            secondAddress = GENERATOR.generateValidAddress();
        } while (firstAddress.equals(secondAddress));
        peer = new SMSPeer(firstAddress);
        SMSPeer other = new SMSPeer(secondAddress);
        assertNotEquals(peer, other);
    }

    @Test
    public void invalidSMSPeerIsValidNegative() {
        assertFalse(SMSPeer.INVALID_SMS_PEER.isValid());
    }
}
