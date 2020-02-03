package ingsw.group1.msglibrary.parsing;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

/**
 * Class containing tests for {@link SMSParsingManager}.
 * Robolectric is used to mock {@link android.content.SharedPreferences} automatically.
 *
 * @author Riccardo De Zen
 */
@Config(sdk = 28)
@RunWith(RobolectricTestRunner.class)
public class SMSParsingManagerTest {

    private static final String EXAMPLE_NAME = "I'm a name";
    private static final String ANOTHER_NAME = "Another name";
    private static final SMSMessage EXAMPLE_MESSAGE = new SMSMessage(
            new RandomSMSPeerGenerator().generateValidPeer(),
            "I'm an example message"
    );

    private static String PREF_FILENAME;
    private static String PREF_KEY_PREFIX;

    private SMSParsingManager parsingManager;
    private String prefKey;
    private Context context;

    /**
     * Example parser to test the set and read feature in {@link SMSParsingManager}. It is
     * relevant for this class to be a private child class because the way their name is written
     * influences the reflection mechanisms used to instantiate the class.
     */
    public static class ExampleSMSParser implements SMSParser {

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
     * Method to initialize the constants used by {@link SMSParsingManager} in order to inject
     * data in the correct preference files.
     */
    @BeforeClass
    public static void setPrefFilenameAndKey() {
        try {
            Field prefFileField = SMSParsingManager.class.getDeclaredField("PREF_FILENAME");
            prefFileField.setAccessible(true);
            PREF_FILENAME = (String) prefFileField.get(null);

            Field prefKeyField = SMSParsingManager.class.getDeclaredField("PREF_KEY_PREFIX");
            prefKeyField.setAccessible(true);
            PREF_KEY_PREFIX = (String) prefKeyField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(
                    "The constants could not be set. Tests cannot be run\n" + e.getMessage()
            );
        }
    }

    /**
     * Method to initialize {@link Context} through {@link RobolectricTestRunner}.
     */
    @Before
    public void initContext() {
        context = ApplicationProvider.getApplicationContext();
        parsingManager = SMSParsingManager.getInstance(context, EXAMPLE_NAME);
        //This should mirror the name defined in the specifications.
        prefKey = PREF_KEY_PREFIX + parsingManager.getInstanceName();
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
     * Method to get the {@link SharedPreferences} file to perform checks.
     *
     * @return The appropriate preference file for this test.
     */
    private SharedPreferences getPreferences() {
        return context.getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE);
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
     * Test to assert the name passed to {@link SMSParsingManager#getInstance(Context, String)}
     * is the name returned by {@link SMSParsingManager#getInstanceName()}.
     */
    @Test
    public void givenNameIsInstanceName() {
        assertEquals(
                EXAMPLE_NAME,
                parsingManager.getInstanceName()
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
     * Test to assert the class can write the parser class in
     * {@link android.content.SharedPreferences}, with file name and key for the value as
     * specified in the specifics.
     *
     * @see SMSParsingManager For the specifics on where the data is stored.
     */
    @Test
    public void canSetParserClass() {
        parsingManager.setOverridingParser(ExampleSMSParser.class);
        assertEquals(getPreferences().getString(prefKey, null), ExampleSMSParser.class.getName());
    }

    /**
     * Test to assert {@link SMSParsingManager#getOverridingParser()} returns {@code null} if no
     * other value was previously set.
     */
    @Test
    public void getOverridingParserNullByDefault() {
        assertNull(parsingManager.getOverridingParser());
    }

    /**
     * Test to assert {@link SMSParsingManager#getOverridingParser()} returns {@code null} if the
     * saved value is not a subclass of {@link SMSParser}. Although this should not be possible
     * through {@link SMSParsingManager} itself, it could be possible to manually insert the
     * value as shown in the test itself.
     */
    @Test
    public void getOverridingParserNullIfNotSubclass() {
        getPreferences().edit().putString(prefKey, Object.class.getName()).commit();
        assertNull(parsingManager.getOverridingParser());
    }

    /**
     * Test to assert {@link SMSParsingManager#getOverridingParser()} returns {@code null} if the
     * saved value is not valid class name. Although this should not be possible
     * through {@link SMSParsingManager} itself, it could be possible to manually insert the
     * value as shown in the test itself.
     */
    @Test
    public void getOverridingParserNullIfNotAClass() {
        getPreferences().edit().putString(prefKey, "No way in Hell I'm a Class").commit();
        assertNull(parsingManager.getOverridingParser());
    }

    /**
     * Test to assert the class can retrieve a previously saved parser Class.
     *
     * @see SMSParsingManager For the specifics on where the data is stored.
     */
    @Test
    public void canGetPreviouslySetClass() {
        parsingManager.setOverridingParser(ExampleSMSParser.class);
        assertEquals(ExampleSMSParser.class, parsingManager.getOverridingParser());
    }

    /**
     * Test to assert {@link SMSParsingManager#setOverridingParser(Class)} returns the old value
     * for the saved parser.
     */
    @Test
    public void setOverridingParserReturnsOldValue() {
        parsingManager.setOverridingParser(ExampleSMSParser.class);
        Class<?> oldValue = parsingManager.setOverridingParser(AnotherExampleSMSParser.class);
        assertEquals(ExampleSMSParser.class, oldValue);
    }

    /**
     * Test to assert {@link SMSParsingManager#setOverridingParser(Class)} resets to null.
     */
    @Test
    public void resetOverridingParserResetsToNull() {
        parsingManager.setOverridingParser(ExampleSMSParser.class);
        parsingManager.resetOverridingParser();
        assertNull(parsingManager.getOverridingParser());
    }

    /**
     * Test asserting the parser actually overrides for outgoing messages, if the saved class is
     * accessible.
     */
    @Test
    public void isOutgoingParsingOverriddenIfAccessible() {
        parsingManager.setOverridingParser(ExampleSMSParser.class);
        SMSMessage actualParsedMessage = parsingManager.parseOutgoingMessage(EXAMPLE_MESSAGE);
        SMSMessage expectedParsedMessage =
                new ExampleSMSParser().parseOutgoingMessage(EXAMPLE_MESSAGE);
        assertTrue(
                expectedParsedMessage.getPeer().equals(actualParsedMessage.getPeer()) &&
                        expectedParsedMessage.getData().equals(actualParsedMessage.getData())
        );
    }

    /**
     * Test asserting the parser actually overrides for incoming messages, if the saved class is
     * accessible.
     */
    @Test
    public void isIncomingParsingOverriddenIfAccessible() {
        parsingManager.setOverridingParser(ExampleSMSParser.class);
        SMSMessage actualParsedMessage = parsingManager.parseIncomingMessage(EXAMPLE_MESSAGE);
        SMSMessage expectedParsedMessage =
                new ExampleSMSParser().parseIncomingMessage(EXAMPLE_MESSAGE);
        assertTrue(
                expectedParsedMessage.getPeer().equals(actualParsedMessage.getPeer()) &&
                        expectedParsedMessage.getData().equals(actualParsedMessage.getData())
        );
    }

    /**
     * Test asserting the parser is not overridden for outgoing messages, if the saved class is
     * inaccessible.
     */
    @Test
    public void outgoingParsingIsNotOverriddenIfInaccessible() {
        parsingManager.setOverridingParser(AnotherExampleSMSParser.class);
        SMSMessage actualParsedMessage = parsingManager.parseOutgoingMessage(EXAMPLE_MESSAGE);
        SMSMessage expectedParsedMessage =
                new ExampleSMSParser().parseOutgoingMessage(EXAMPLE_MESSAGE);
        assertFalse(
                expectedParsedMessage.getPeer().equals(actualParsedMessage.getPeer()) &&
                        expectedParsedMessage.getData().equals(actualParsedMessage.getData())
        );
    }

    /**
     * Test asserting the parser is not overridden for incoming messages, if the saved class is
     * inaccessible.
     */
    @Test
    public void incomingParsingIsNotOverriddenIfInaccessible() {
        parsingManager.setOverridingParser(AnotherExampleSMSParser.class);
        SMSMessage actualParsedMessage = parsingManager.parseIncomingMessage(EXAMPLE_MESSAGE);
        SMSMessage expectedParsedMessage =
                new ExampleSMSParser().parseIncomingMessage(EXAMPLE_MESSAGE);
        assertFalse(
                expectedParsedMessage.getPeer().equals(actualParsedMessage.getPeer()) &&
                        expectedParsedMessage.getData().equals(actualParsedMessage.getData())
        );
    }
}
