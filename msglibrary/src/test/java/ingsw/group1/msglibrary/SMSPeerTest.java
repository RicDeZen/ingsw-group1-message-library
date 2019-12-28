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
 */
@RunWith(Parameterized.class)
public class SMSPeerTest {

    public static final int TEST_RUNS = 100;
    private final int run;

    /**
     * @return parameters for the test. Just the index of the test run.
     */
    @Parameterized.Parameters
    public static Object[] data() {
        Integer[] params = new Integer[TEST_RUNS];
        for(int i = 0; i < TEST_RUNS; i++)
            params[i] = i;
        return params;
    }

    /**
     * @param run test index.
     */
    public SMSPeerTest(int run){
        this.run = run;
    }

    private static final RandomSMSPeerGenerator GENERATOR = new RandomSMSPeerGenerator();
    private SMSPeer peer;

    @Test(expected = InvalidAddressException.class)
    public void constructorFails(){
        String invalidAddress = GENERATOR.generateInvalidAddress();
        peer = new SMSPeer(invalidAddress);
    }

    @Test
    public void constructorPasses(){
        peer = new SMSPeer(GENERATOR.generateValidAddress());
        assertNotNull(peer);
    }

    @Test
    public void getAddress(){
        String address = GENERATOR.generateValidAddress();
        peer = new SMSPeer(address);
        assertEquals(address, peer.getAddress());
    }

    @Test
    public void equalsSame(){
        String address = GENERATOR.generateValidAddress();
        peer = new SMSPeer(address);
        SMSPeer other = new SMSPeer(address);
        assertEquals(peer, peer);
        assertEquals(peer, other);
    }

    @Test
    public void notEqualsDifferent(){
        String firstAddress = GENERATOR.generateValidAddress();
        String secondAddress = GENERATOR.generateValidAddress();
        while(firstAddress.equals(secondAddress))
            secondAddress = GENERATOR.generateValidAddress();
        peer = new SMSPeer(firstAddress);
        SMSPeer other = new SMSPeer(secondAddress);
        assertNotEquals(peer, other);
    }

    @Test
    public void invalidSMSPeerIsValidNegative(){
        assertFalse(SMSPeer.INVALID_SMS_PEER.isValid());
    }
}
