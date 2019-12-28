package ingsw.group1.msglibrary;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertNotSame;
import static junit.framework.TestCase.assertSame;
import static junit.framework.TestCase.assertTrue;

@RunWith(Parameterized.class)
public class RandomSMSPeerGeneratorTest {

    private static final RandomSMSPeerGenerator GENERATOR = new RandomSMSPeerGenerator();

    private final String countryCode;

    /**
     * @return parameters for the test:
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
    public void canCreateDefaultRegionValidAddress() {
        assertSame(SMSPeer.PhoneNumberValidity.VALID,SMSPeer.getAddressValidity(GENERATOR.generateValidAddress()));
    }

    @Test
    public void canCreateDefaultRegionInvalidAddress() {
        assertNotSame(SMSPeer.PhoneNumberValidity.VALID, SMSPeer.getAddressValidity(GENERATOR.generateInvalidAddress(countryCode)));
    }

    @Test
    public void canCreateDefaultRegionValidPeer() {
        assertTrue(GENERATOR.generateValidPeer().isValid());
    }

    @Test
    public void canCreateValidAddress() {
        assertSame(SMSPeer.PhoneNumberValidity.VALID,SMSPeer.getAddressValidity(GENERATOR.generateValidAddress(countryCode)));
    }

    @Test
    public void canCreateInvalidAddress() {
        assertNotSame(SMSPeer.PhoneNumberValidity.VALID, SMSPeer.getAddressValidity(GENERATOR.generateInvalidAddress(countryCode)));
    }

    @Test
    public void canCreateValidPeer() {
        assertTrue(GENERATOR.generateValidPeer(countryCode).isValid());
    }
}
