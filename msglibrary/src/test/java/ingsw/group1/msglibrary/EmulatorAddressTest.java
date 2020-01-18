package ingsw.group1.msglibrary;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ingsw.group1.msglibrary.exceptions.InvalidAddressException;

import static junit.framework.TestCase.fail;

/**
 * Testing Peers with emulator numbers.
 *
 * @author Riccardo De Zen
 */
@RunWith(Parameterized.class)
public class EmulatorAddressTest {

    private static final String[] HEADS = new String[]{"555", "+555", "+1555521555"};
    private static final String[] TAILS = new String[]{"4", "6", "8"};

    private String addressToTest;

    /**
     * Parameters for the test, in the form of the various possible emulator addresses.
     */
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object> params() {
        List<Object> parameters = new ArrayList<>();
        for (String head : HEADS) {
            for (String tail : TAILS) {
                String address = head + tail;
                parameters.add(address);
            }
        }
        return parameters;
    }

    /**
     * Public constructor for the test.
     *
     * @param parameterAddress The address to be tested.
     */
    public EmulatorAddressTest(String parameterAddress) {
        addressToTest = parameterAddress;
    }

    /**
     * Testing whether the Constructor actually accepts an emulator address.
     */
    @Test
    public void emulatorAddressConstructorPasses() {
        try {
            new SMSPeer(addressToTest);
        } catch (InvalidAddressException e) {
            fail();
        }
    }
}
