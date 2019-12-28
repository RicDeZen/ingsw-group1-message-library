package ingsw.group1.msglibrary;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertFalse;

/**
 * This test class uses {@link PhoneNumberUtil#parse(String, String)} to validate the generated
 * number instead of methods in SMSPeer to avoid a loop in dependencies.
 *
 * @author Riccardo De Zen
 */
@RunWith(Parameterized.class)
public class RandomSMSPeerGeneratorTest {

    private static final String DEFAULT_REGION = SMSPeer.DEFAULT_REGION;
    private static final PhoneNumberUtil UTIL = PhoneNumberUtil.getInstance();
    private static final RandomSMSPeerGenerator GENERATOR = new RandomSMSPeerGenerator();

    private final String countryCode;

    /**
     * @return parameters for the test:
     * - Region to test.
     */
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<String> data() {
        String[] supportedCountries =
                PhoneNumberUtil.getInstance().getSupportedRegions().toArray(new String[0]);
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
        String stringNumber = GENERATOR.generateValidAddress();
        try{
            Phonenumber.PhoneNumber number = UTIL.parse(stringNumber, DEFAULT_REGION);
            assertTrue(UTIL.isValidNumber(number));
        }
        catch (NumberParseException e){
            fail();
        }
    }

    @Test
    public void canCreateDefaultRegionInvalidAddress() {
        String stringNumber = GENERATOR.generateInvalidAddress();
        try{
            Phonenumber.PhoneNumber number = UTIL.parse(stringNumber, DEFAULT_REGION);
            assertFalse(UTIL.isValidNumber(number));
        }
        catch (NumberParseException e){
            assertTrue(true);
        }
    }

    @Test
    public void canCreateValidAddress() {
        String stringNumber = GENERATOR.generateValidAddress(countryCode);
        try{
            Phonenumber.PhoneNumber number = UTIL.parse(stringNumber, countryCode);
            assertTrue(UTIL.isValidNumber(number));
        }
        catch (NumberParseException e){
            fail();
        }
    }

    @Test
    public void canCreateInvalidAddress() {
        String stringNumber = GENERATOR.generateInvalidAddress(countryCode);
        try{
            Phonenumber.PhoneNumber number = UTIL.parse(stringNumber, countryCode);
            assertFalse(UTIL.isValidNumber(number));
        }
        catch (NumberParseException e){
            assertTrue(true);
        }
    }

    /**
     * The two following tests should be assumed passed only if the tests for SMSPeer passed.
     * Such tests depend on the result of the previous four.
     */

    @Test
    public void canCreateDefaultRegionValidPeer() {
        assertTrue(GENERATOR.generateValidPeer().isValid());
    }

    @Test
    public void canCreateValidPeer() {
        assertTrue(GENERATOR.generateValidPeer(countryCode).isValid());
    }
}
