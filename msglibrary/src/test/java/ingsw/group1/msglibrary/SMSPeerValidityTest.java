package ingsw.group1.msglibrary;

import android.os.Build;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ingsw.group1.msglibrary.exceptions.InvalidAddressException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
     * @return the Parameters for the test, in the form:
     * - Run number.
     * - Expected Validity.
     * First half of the runs is for valid numbers. Second half is for invalid numbers.
     */
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<String> data() {
        String[] supportedCountries = PhoneNumberUtil.getInstance().getSupportedRegions().toArray(new String[0]);
        return Arrays.asList(supportedCountries);
    }

    /**
     * Default constructor. Declaration in parameterized tests is needed even if empty.
     */
    public SMSPeerValidityTest(String countryCode) {
        this.countryCode = countryCode;
    }

    @Test
    public void constructorAcceptsValid() {
        new SMSPeer(GENERATOR.generateValidAddress(countryCode));
    }

    @Test(expected = InvalidAddressException.class)
    public void constructorThrowsForInvalid(){
        new SMSPeer(GENERATOR.generateInvalidAddress(countryCode));
    }
}
