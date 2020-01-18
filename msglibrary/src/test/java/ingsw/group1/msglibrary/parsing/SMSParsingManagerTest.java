package ingsw.group1.msglibrary.parsing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import ingsw.group1.msglibrary.SMSMessage;

/**
 * Class containing tests for {@link SMSParsingManager}.
 * Robolectric is used to mock {@link android.content.SharedPreferences} automatically.
 *
 * @author Riccardo De Zen
 */
@RunWith(RobolectricTestRunner.class)
public class SMSParsingManagerTest {

    private static final String PREF_FILENAME = SMSParsingManager.PREF_FILENAME;
    private static final String PREF_KEY = SMSParsingManager.PREF_KEY;

    private SMSParsingManager parsingManager;

    /**
     * Test to assert the class works as a proper key-based Object Pool: same key means same
     * instance.
     */
    @Test
    public void sameNameInstancesAreSame() {

    }

    /**
     * Test to assert the class works as a proper key-based Object Pool: different key means
     * different instance.
     */
    @Test
    public void differentNameInstancesAreDifferent() {

    }

    /**
     * Test to assert {@link SMSParsingManager#parseOutgoingMessage(SMSMessage)} actually does
     * nothing by default.
     */
    @Test
    public void parseOutgoingDoesNothingByDefault() {

    }

    /**
     * Test to assert {@link SMSParsingManager#parseIncomingMessage(SMSMessage)} actually does
     * nothing by default.
     */
    @Test
    public void parseIncomingDoesNothingByDefault() {

    }

    /**
     * Test to assert the class can write the parser class in
     * {@link android.content.SharedPreferences}.
     */
    @Test
    public void canSetParser() {

    }

    /**
     * Test to assert {@link SMSParsingManager#setOverridingParser(Class)} returns the old value
     * for the saved parser.
     */
    @Test
    public void setParsingManagerReturnsOldValue() {

    }

    /**
     * Test asserting the class can get the previously saved parser class.
     */
    @Test
    public void canGetParser() {

    }

    /**
     * Test asserting the parser actually overrides for outgoing messages.
     */
    @Test
    public void isOutgoingParsingOverridden() {

    }

    /**
     * Test asserting the parser actually overrides for incoming messages.
     */
    @Test
    public void isIncomingParsingOverridden() {

    }
}
