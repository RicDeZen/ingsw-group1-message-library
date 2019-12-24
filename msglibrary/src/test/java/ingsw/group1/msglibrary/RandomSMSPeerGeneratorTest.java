package ingsw.group1.msglibrary;

import android.util.ArraySet;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static junit.framework.TestCase.assertNotSame;
import static junit.framework.TestCase.assertSame;
import static junit.framework.TestCase.assertTrue;

@RunWith(Parameterized.class)
public class RandomSMSPeerGeneratorTest {

    private static List<String> COUNTRIES = new ArrayList<>();

    private RandomSMSPeerGenerator generator = new RandomSMSPeerGenerator();

    private final int run;
    private final String countryCode;

    /**
     * @return parameters for the test:
     * - Index for the run.
     * - Region to test.
     */
    @Parameterized.Parameters(name = "{0}: {1}")
    public static Collection<Object[]> data() {
        String[] array = PhoneNumberUtil.getInstance().getSupportedRegions().toArray(new String[0]);
        COUNTRIES = Arrays.asList(array);
        List<Object[]> params = new ArrayList<>();
        for(int i = 0; i < COUNTRIES.size(); i++){
            params.add(new Object[]{
                    i, COUNTRIES.get(i)
            });
        }
        return params;
    }

    /**
     * @param run test index.
     */
    public RandomSMSPeerGeneratorTest(int run, String countryCode) {
        this.run = run;
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
