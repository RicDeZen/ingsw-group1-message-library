package ingsw.group1.msglibrary;

import android.os.Build;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collection;

import ingsw.group1.msglibrary.exceptions.InvalidAddressException;

import static junit.framework.TestCase.assertTrue;

/**
 * @author Giorgia Bortoletti
 * @author Riccardo De Zen
 */
@Config(sdk = Build.VERSION_CODES.P)
@RunWith(Parameterized.class)
public class SMSPeerValidityTest {

    private static final RandomSMSPeerGenerator GENERATOR = new RandomSMSPeerGenerator();

    private final String countryCode;

    /**
     * @return the Parameters for the test. The parameters are the list of supported countries.
     * First half of the runs is for valid numbers. Second half is for invalid numbers.
     */
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<String> data() {
        String[] supportedCountries = PhoneNumberUtil.getInstance().getSupportedRegions().toArray(new String[0]);
        return Arrays.asList(supportedCountries);
    }

    /**
     * Default constructor.
     * @param countryCode the code for the country to test.
     */
    public SMSPeerValidityTest(String countryCode) {
        this.countryCode = countryCode;
    }

    @Test
    public void constructorAcceptsValid() {
        new SMSPeer(GENERATOR.generateValidAddress(countryCode));
        assertTrue(true);
    }

    @Test(expected = InvalidAddressException.class)
    public void constructorThrowsForInvalid(){
        new SMSPeer(GENERATOR.generateInvalidAddress(countryCode));
    }
}
