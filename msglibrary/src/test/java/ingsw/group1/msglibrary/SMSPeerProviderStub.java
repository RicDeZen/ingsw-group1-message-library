package ingsw.group1.msglibrary;

import android.os.Build;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Class to generate random phone numbers following the SMSPeer criteria
 * @author Riccardo De Zen
 */
@Config(sdk = Build.VERSION_CODES.P)
@RunWith(RobolectricTestRunner.class)
public class SMSPeerProviderStub implements PeerProviderStub<SMSPeer>{

    private static final String INTERNATIONAL_PREFIX = "+";
    private static final String DEFAULT_COUNTRY_CODE = "IT";

    /**
     * Precise criteria for valid numbers are not limited to the ones described in SMSPeer so there are
     * some magic numbers.
     * @return a randomly generated valid Peer
     */
    @Override
    public SMSPeer getRandomPeer() {
        return new SMSPeer(getValidAddress());
    }

    private String getValidAddress(){
        Phonenumber.PhoneNumber number = PhoneNumberUtil.getInstance().getExampleNumber(DEFAULT_COUNTRY_CODE);
        return INTERNATIONAL_PREFIX + number.getCountryCode() + number.getNationalNumber();
    }

    @Test
    public void TestAFewNumbers(){
        for(int i = 0; i < 1000; i++)
            getRandomPeer();
    }
}
