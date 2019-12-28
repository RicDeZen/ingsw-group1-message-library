package ingsw.group1.msglibrary;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertNotSame;
import static junit.framework.TestCase.assertSame;
import static junit.framework.TestCase.assertTrue;

@RunWith(Parameterized.class)
public class RandomSMSPeerGeneratorTest {

    private RandomSMSPeerGenerator generator = new RandomSMSPeerGenerator();

    private final String countryCode;

    /**
     * @return parameters for the test:
     * - Index for the run.
     * - Region to test.
     */
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<String> data() {
        String[] supportedCountries = PhoneNumberUtil.getInstance().getSupportedRegions().toArray(new String[0]);
        return Arrays.asList(supportedCountries);
    }

    /**
     * @param countryCode the country code for which we want to run the tests.
     */
    public RandomSMSPeerGeneratorTest(String countryCode) {
        this.countryCode = countryCode;
    }

    @Test
    public void canCreateValidPeer() {
        assertTrue(generator.generateValidPeer(countryCode).isValid());
    }

    @Test
    public void canCreateValidAddress() {
        assertSame(SMSPeer.PhoneNumberValidity.VALID,SMSPeer.getAddressValidity(generator.generateValidAddress(countryCode)));
    }

    @Test
    public void canCreateInvalidAddress() {
        assertNotSame(SMSPeer.PhoneNumberValidity.VALID, SMSPeer.getAddressValidity(generator.generateInvalidAddress(countryCode)));
    }
}
