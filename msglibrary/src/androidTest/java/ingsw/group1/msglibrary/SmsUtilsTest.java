package ingsw.group1.msglibrary;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Riccardo De Zen
 */
public class SmsUtilsTest {

    private String formattedNumber = "+393928402034";
    private String validNumber = "3928402034";
    private String invalidNumber = "12345";
    private String validCountryCode = "IT";
    private String invalidCountryCode = "it";

    @Test
    public void formatSMSNumberNullNumber() {
        assertNull(SmsUtils.formatSMSNumber(null, null));
        assertNull(SmsUtils.formatSMSNumber(null,validCountryCode));
    }

    @Test
    public void formatSMSNumberNullCountryCode() {
        assertEquals(invalidNumber,SmsUtils.formatSMSNumber(invalidNumber, null));
        assertEquals(validNumber,SmsUtils.formatSMSNumber(validNumber,null));
    }

    @Test
    public void formatSMSNumberInvalidCountryCode() {
        assertEquals(invalidNumber,SmsUtils.formatSMSNumber(invalidNumber, invalidCountryCode));
        assertEquals(validNumber, SmsUtils.formatSMSNumber(validNumber,invalidCountryCode));
    }

    @Test
    public void formatSMSNumberValidCountryCode() {
        assertEquals(invalidNumber,SmsUtils.formatSMSNumber(invalidNumber,validCountryCode));
        assertEquals(formattedNumber, SmsUtils.formatSMSNumber(validNumber, validCountryCode));
    }

    @Test
    public void isMessagePertinentTrue(){
        String message = SMSHandler.APP_KEY;
        assertTrue(SmsUtils.isMessagePertinent(message));
    }

    @Test
    public void isMessagePertinentFalse(){
        String message = "Hello" + SMSHandler.APP_KEY;
        assertFalse(SmsUtils.isMessagePertinent(message));
    }
}