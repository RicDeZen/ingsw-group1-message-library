package ingsw.group1.msglibrary.parsing;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import ingsw.group1.msglibrary.SMSMessage;

/**
 * Class implementing {@link MessageParser} for {@link SMSMessage}.
 * The class behaves as an Object Pool, requiring a String name to get an appropriate instance.
 * By default also acts as a parser that does no operation on the messages. Allows setting a new
 * parser to override the one in use by the current instance by calling
 * {@link SMSParsingManager#setOverridingParser(Class)}, such a change persists, since it is
 * stored in {@link android.content.SharedPreferences}, in a file called
 * {@link SMSParsingManager#PREF_FILENAME} with a key which is:
 * {@link SMSParsingManager#PREF_KEY_PREFIX} + {@link SMSParsingManager#instanceName}.
 * <p>
 * In order to be instantiated the Parser class needs a public constructor with no parameters.
 * Since the raw class name is stored, generics are not supported. If you need one of the
 * previously stated features, move the parsing logic to an upper level.
 * <p>
 * Also allows resetting to the default parsing strategy by calling
 * {@link SMSParsingManager#resetOverridingParser()}.
 *
 * @author Riccardo De Zen
 */
public class SMSParsingManager implements SMSParser {

    //Base name for Preference file name.
    static final String PREF_FILENAME = "ingsw.group1.msglibrary.parsing.SMSParsingManager";
    //Base name for Preference key. Should be followed by instance name.
    static final String PREF_KEY_PREFIX = "defaultParser-";
    //Map containing the active instances of this class.
    private static Map<String, SMSParsingManager> activeInstances = new HashMap<>();

    private final String PREF_KEY;
    private SharedPreferences parsingPreferences;
    private String instanceName;

    @Nullable
    private SMSParser overridingParser;

    /**
     * Private constructor for this class.
     *
     * @param context      The calling Context. Must not be {@code null}.
     * @param instanceName The name for this instance. Must not be {@code null}.
     */
    private SMSParsingManager(@NonNull Context context, @NonNull String instanceName) {
        this.parsingPreferences = getPreferences(context);
        this.instanceName = instanceName;
        this.PREF_KEY = PREF_KEY_PREFIX + instanceName;
        this.overridingParser = instantiateOverridingParser();
    }

    /**
     * Method to get an Instance of this class, associated with the appropriate name.
     *
     * @param context The calling {@link Context}, used to access
     *                {@link android.content.SharedPreferences} to store the name of the
     *                overriding parser class. Must not be {@code null}.
     * @param name    The name for the instance. Must not be {@code null}.
     * @return A properly initialized instance of SMSParser.
     */
    @NonNull
    public static SMSParsingManager getInstance(@NonNull Context context, @NonNull String name) {
        SMSParsingManager existingInstance = activeInstances.get(name);
        if (existingInstance != null)
            return existingInstance;
        SMSParsingManager newInstance = new SMSParsingManager(context, name);
        activeInstances.put(name, newInstance);
        return newInstance;
    }

    /**
     * Getter for {@link SMSParsingManager#instanceName}.
     *
     * @return {@link SMSParsingManager#instanceName}, the name for this instance.
     */
    @NonNull
    public String getInstanceName() {
        return instanceName;
    }

    /**
     * Method to retrieve the {@link SharedPreferences}.
     *
     * @param context The {@link Context} from which to retrieve the {@link SharedPreferences}.
     * @return The appropriate instance of {@link SharedPreferences}.
     */
    private SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(
                PREF_FILENAME,
                Context.MODE_PRIVATE
        );
    }

    /**
     * Method to save a value in the appropriate field {@link SharedPreferences}.
     * Apply is used as suggested by the compiler in order to write to Preferences in the
     * background, to let Android framework handle failures properly.
     *
     * @param newValue The new value for the field, can be null.
     * @return The previously stored value.
     */
    @Nullable
    private String storeInPreferences(@Nullable String newValue) {
        String oldValue = parsingPreferences.getString(PREF_KEY, null);
        SharedPreferences.Editor prefEditor = parsingPreferences.edit();
        prefEditor.putString(PREF_KEY, newValue);
        prefEditor.apply();
        return oldValue;
    }

    /**
     * Method setting the new Parser, this method saves the class' name in the
     * {@link SharedPreferences}, in a file called {@link SMSParsingManager#PREF_FILENAME}, in a
     * field called: {@link SMSParsingManager#PREF_KEY_PREFIX} +
     * {@link SMSParsingManager#instanceName}.
     *
     * @param newParser A reference to the class defining the new overriding parser.
     * @return The previously stored class, no type check are performed on the stored class
     * except whether it extends {@link SMSParser}. Returns {@code null} if the class could not
     * be found or it did not extend {@link SMSParser}.
     */
    @Nullable
    public Class<? extends SMSParser> setOverridingParser(@NonNull Class<? extends SMSParser> newParser) {
        Class<? extends SMSParser> oldStoredClass = getOverridingParser();
        storeInPreferences(newParser.getName());
        overridingParser = instantiateOverridingParser();
        return oldStoredClass;
    }

    /**
     * Getter for the saved parser class.
     *
     * @return The reference to the currently set {@link SMSParser} class. The Type of stored
     * class is not questioned (as long as it extends {@link SMSParser}).
     */
    @Nullable
    public Class<? extends SMSParser> getOverridingParser() {
        String storedValue = parsingPreferences.getString(PREF_KEY, null);
        if (storedValue == null) return null;
        try {
            Class<?> storedClass = Class.forName(storedValue);
            if (SMSParser.class.isAssignableFrom(storedClass))
                //The cast is safe due to the above check.
                return (Class<? extends SMSParser>) storedClass;
            else
                return null;
        } catch (ClassNotFoundException e) {
            //The class might have been renamed or its name might have been inappropriately stored.
            return null;
        }
    }

    /**
     * Method resetting the parser this instance uses to the default (no operation on messages).
     *
     * @return The reference to the previously set {@link SMSParser} class. The Type of stored
     * class is not questioned (as long as it extends {@link SMSParser}).
     */
    @Nullable
    public Class<? extends SMSParser> resetOverridingParser() {
        Class<? extends SMSParser> oldStoredClass = getOverridingParser();
        storeInPreferences(null);
        overridingParser = instantiateOverridingParser();
        return oldStoredClass;
    }

    /**
     * Method to create an instance of the Overriding parser class. If the class cannot be
     * instantiated, the instance is left {@code null};
     *
     * @return The instance of the stored class, can be {@code null}.
     */
    @Nullable
    private SMSParser instantiateOverridingParser() {
        Class<? extends SMSParser> overridingClass = getOverridingParser();
        if (overridingClass == null) return null;
        try {
            return overridingClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            return null;
        }
    }

    /**
     * @param message The outgoing {@link SMSMessage}. This is not modified, a new one is created.
     * @return The formatted {@link SMSMessage}, ready to be sent.
     */
    @Override
    public SMSMessage parseOutgoingMessage(SMSMessage message) {
        if (overridingParser == null) return message;
        return overridingParser.parseOutgoingMessage(message);
    }

    /**
     * @param message The incoming {@link SMSMessage}.
     * @return The parsed {@link SMSMessage}, as it would be before a
     * {@link SMSParsingManager#parseOutgoingMessage(SMSMessage)} call.
     */
    @Override
    public SMSMessage parseIncomingMessage(SMSMessage message) {
        if (overridingParser == null) return message;
        return overridingParser.parseIncomingMessage(message);
    }
}
