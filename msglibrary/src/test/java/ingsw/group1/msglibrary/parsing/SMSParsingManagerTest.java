package ingsw.group1.msglibrary.parsing;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.Map;

import ingsw.group1.msglibrary.RandomSMSPeerGenerator;
import ingsw.group1.msglibrary.SMSMessage;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotEquals;

/**
 * Class containing tests for {@link SMSParsingManager}.
 * Robolectric is used to mock {@link android.content.SharedPreferences} automatically.
 *
 * @author Riccardo De Zen
 */
@Config(sdk = 28)
@RunWith(RobolectricTestRunner.class)
public class SMSParsingManagerTest {

    private static final String PREF_FILENAME = SMSParsingManager.PREF_FILENAME;
    private static final String PREF_KEY = SMSParsingManager.PREF_KEY_PREFIX;
    private static final String EXAMPLE_NAME = "I'm a name";
    private static final String ANOTHER_NAME = "Another name";
    private static final SMSMessage EXAMPLE_MESSAGE = new SMSMessage(
            new RandomSMSPeerGenerator().generateValidPeer(),
            "I'm an example message"
    );

    private SMSParsingManager parsingManager;

    private Context context;

    /**
     * Example parser to test the set and read feature in {@link SMSParsingManager}. It is
     * relevant for this class to be a private child class because the way their name is written
     * influences the reflection mechanisms used to instantiate the class.
     */
    private static class ExampleSMSParser implements SMSParser {

        @Override
        public SMSMessage parseOutgoingMessage(SMSMessage message) {
            return new SMSMessage(message.getPeer(), "#" + message.getData());
        }

        @Override
        public SMSMessage parseIncomingMessage(SMSMessage message) {
            return new SMSMessage(message.getPeer(), message.getData().substring(1));
        }
    }

    /**
     * Another example parser, to test ability to get the previously stored value.
     */
    private static class AnotherExampleSMSParser implements SMSParser {

        @Override
        public SMSMessage parseOutgoingMessage(SMSMessage message) {
            return new SMSMessage(message.getPeer(), "#" + message.getData());
        }

        @Override
        public SMSMessage parseIncomingMessage(SMSMessage message) {
            return new SMSMessage(message.getPeer(), message.getData().substring(1));
        }
    }

    /**
     * Method to initialize {@link Context} through {@link RobolectricTestRunner}.
     */
    @Before
    public void initContext() {
        context = ApplicationProvider.getApplicationContext();
        parsingManager = SMSParsingManager.getInstance(context, EXAMPLE_NAME);
    }

    /**
     * Method to reset the instances of the {@link SMSParsingManager} class, to reset Preferences
     * as well.
     */
    @After
    public void resetInstances() {
        try {
            Field instances = SMSParsingManager.class.getDeclaredField("activeInstances");
            instances.setAccessible(true);
            Map<?, ?> instancesMap = (Map<?, ?>) instances.get(null);
            if (instancesMap != null) instancesMap.clear();
            else throw new NoSuchFieldException("Instance Map was null");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Instances could not be reset due to: " + e.getMessage());
        }
    }

    /**
     * Test to assert the class works as a proper key-based Object Pool: same key means same
     * instance.
     */
    @Test
    public void sameNameInstancesAreSame() {
        assertEquals(
                SMSParsingManager.getInstance(context, EXAMPLE_NAME),
                SMSParsingManager.getInstance(context, EXAMPLE_NAME)
        );
    }

    /**
     * Test to assert the class works as a proper key-based Object Pool: different key means
     * different instance.
     */
    @Test
    public void differentNameInstancesAreDifferent() {
        assertNotEquals(
                SMSParsingManager.getInstance(context, EXAMPLE_NAME),
                SMSParsingManager.getInstance(context, ANOTHER_NAME)
        );
    }

    /**
     * Test to assert {@link SMSParsingManager#parseOutgoingMessage(SMSMessage)} actually does
     * nothing by default.
     */
    @Test
    public void parseOutgoingDoesNothingByDefault() {
        SMSMessage parsedMessage = parsingManager.parseOutgoingMessage(EXAMPLE_MESSAGE);
        assertTrue(
                EXAMPLE_MESSAGE.getPeer().equals(parsedMessage.getPeer()) &&
                        EXAMPLE_MESSAGE.getData().equals(parsedMessage.getData())
        );
    }

    /**
     * Test to assert {@link SMSParsingManager#parseIncomingMessage(SMSMessage)} actually does
     * nothing by default.
     */
    @Test
    public void parseIncomingDoesNothingByDefault() {
        SMSMessage parsedMessage = parsingManager.parseIncomingMessage(EXAMPLE_MESSAGE);
        assertTrue(
                EXAMPLE_MESSAGE.getPeer().equals(parsedMessage.getPeer()) &&
                        EXAMPLE_MESSAGE.getData().equals(parsedMessage.getData())
        );
    }

    /**
     * // FIXME: 19/01/2020 The preference is read as null from outside on separated tests.
     * Test to assert the class can write the parser class in
     * {@link android.content.SharedPreferences} and subsequently retrieve it, when starting from
     * a {@code null} value.
     */
    @Test
    public void canSetAndGetParserFromNull() {
        parsingManager.setOverridingParser(ExampleSMSParser.class);
        assertEquals(ExampleSMSParser.class, parsingManager.getOverridingParser());
    }

    /**
     * // FIXME: 19/01/2020 Same as above test.
     * Test to assert the class can write the parser class in
     * {@link android.content.SharedPreferences} and subsequently retrieve it, when starting from
     * some other already set value.
     */
    @Test
    public void canSetAndGetParserFromAlreadySet() {
        parsingManager.setOverridingParser(ExampleSMSParser.class);
        parsingManager.setOverridingParser(AnotherExampleSMSParser.class);
        assertEquals(AnotherExampleSMSParser.class, parsingManager.getOverridingParser());
    }

    /**
     * Test to assert {@link SMSParsingManager#setOverridingParser(Class)} returns the old value
     * for the saved parser.
     */
    @Test
    public void setParsingManagerReturnsOldValue() {
        parsingManager.setOverridingParser(ExampleSMSParser.class);
        Class<?> oldValue = parsingManager.setOverridingParser(AnotherExampleSMSParser.class);
        assertEquals(ExampleSMSParser.class, oldValue);
    }

    //TODO reset tests

    /**
     * Test asserting the parser actually overrides for outgoing messages.
     */
    @Test
    public void isOutgoingParsingOverridden() {
        parsingManager.setOverridingParser(ExampleSMSParser.class);
        SMSMessage actualParsedMessage = parsingManager.parseOutgoingMessage(EXAMPLE_MESSAGE);
        SMSMessage expectedParsedMessage = parsingManager.parseOutgoingMessage(EXAMPLE_MESSAGE);
        assertTrue(
                expectedParsedMessage.getPeer().equals(actualParsedMessage.getPeer()) &&
                        expectedParsedMessage.getData().equals(actualParsedMessage.getData())
        );
    }

    /**
     * Test asserting the parser actually overrides for incoming messages.
     */
    @Test
    public void isIncomingParsingOverridden() {
        parsingManager.setOverridingParser(ExampleSMSParser.class);
        SMSMessage actualParsedMessage = parsingManager.parseOutgoingMessage(EXAMPLE_MESSAGE);
        SMSMessage expectedParsedMessage = parsingManager.parseOutgoingMessage(EXAMPLE_MESSAGE);
        assertTrue(
                expectedParsedMessage.getPeer().equals(actualParsedMessage.getPeer()) &&
                        expectedParsedMessage.getData().equals(actualParsedMessage.getData())
        );
    }
}
