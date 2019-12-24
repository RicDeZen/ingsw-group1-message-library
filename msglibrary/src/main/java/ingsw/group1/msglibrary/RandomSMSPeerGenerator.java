package ingsw.group1.msglibrary;

import androidx.annotation.NonNull;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Random;
import java.util.jar.Manifest;

/**
 * Utility class to generate random instances of {@code SMSPeer}. Useful in testing.
 *
 * @author Riccardo De Zen
 */
public class RandomSMSPeerGenerator implements RandomPeerGenerator<String, SMSPeer> {

    private static final Random random = new Random();
    private static final PhoneNumberUtil utils = PhoneNumberUtil.getInstance();

    /**
     * @return a valid built {@code SMSPeer} for the default region.
     */
    @Override
    public SMSPeer generateValidPeer() {
        return new SMSPeer(generateValidAddress());
    }

    /**
     * @param region the region for the {@code SMSPeer}.
     * @return a valid built SMSPeer for the given region.
     */
    public SMSPeer generateValidPeer(String region) {
        return new SMSPeer(generateValidAddress(region));
    }

    /**
     * @return an example of valid address for the default region.
     */
    @Override
    public String generateValidAddress() {
        String address;
        do {
            Phonenumber.PhoneNumber number = utils.getExampleNumber(SMSPeer.DEFAULT_REGION);
            randomizeNumber(number);
            address = utils.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
        } while (SMSPeer.getAddressValidity(address) != SMSPeer.PhoneNumberValidity.VALID);
        return address;
    }

    /**
     * @param region the region for the {@code SMSPeer} address.
     * @return an example of valid address for the given region.
     */
    public String generateValidAddress(String region) {
        String address;
        do {
            Phonenumber.PhoneNumber number = utils.getExampleNumber(region);
            randomizeNumber(number);
            address = utils.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
        } while (SMSPeer.getAddressValidity(address) != SMSPeer.PhoneNumberValidity.VALID);
        return address;
    }

    /**
     * @return an invalid address for an {@code SMSPeer}.
     */
    @Override
    public String generateInvalidAddress() {
        String address;
        do {
            Phonenumber.PhoneNumber number = utils.getInvalidExampleNumber(SMSPeer.DEFAULT_REGION);
            randomizeNumber(number);
            address = utils.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
        } while (SMSPeer.getAddressValidity(address) == SMSPeer.PhoneNumberValidity.VALID);
        return address;
    }

    /**
     * @return an invalid address for an {@code SMSPeer}.
     */
    public String generateInvalidAddress(String region) {
        String address;
        do {
            Phonenumber.PhoneNumber number = utils.getInvalidExampleNumber(region);
            randomizeNumber(number);
            address = utils.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
        } while (SMSPeer.getAddressValidity(address) == SMSPeer.PhoneNumberValidity.VALID);
        return address;
    }

    /**
     * @param number a starting number.
     * @return a randomized number.
     */
    private void randomizeNumber(Phonenumber.PhoneNumber number) {
        long nationalNumber = number.getNationalNumber();
        long randomized = Math.abs(random.nextLong() % nationalNumber) + 1;
        number.setNationalNumber(randomized);
    }
}
