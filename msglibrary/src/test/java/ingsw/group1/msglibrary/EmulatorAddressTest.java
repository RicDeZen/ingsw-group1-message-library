package ingsw.group1.msglibrary;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ingsw.group1.msglibrary.exceptions.InvalidAddressException;

import static junit.framework.TestCase.fail;

/**
 * Testing Peers with emulator numbers.
 *
 * @author Riccardo De Zen
 */
@RunWith(Parameterized.class)
public class EmulatorAddressTest {

    private String addressToTest;

    /**
     * Parameters for the test, in the form of the various possible emulator addresses.
     */
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Object[][] params() {
        return new Object[][]{
                {"5554"},
                {"+15555215554"},
                {"5556"},
                {"+15555215556"},
                {"5558"},
                {"+15555215558"},
        };
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
