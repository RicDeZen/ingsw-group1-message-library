package ingsw.group1.msglibrary.parsing;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.Map;

import ingsw.group1.msglibrary.SMSMessage;

/**
 * Class implementing {@link MessageParser} for {@link SMSMessage}.
 * The class behaves as an Object Pool, requiring a String name to get an appropriate instance.
 * By default also acts as a parser that does no operation on the messages. Allows setting a new
 * parser to override the one in use by the current instance by calling
 * {@link SMSParsingManager#setOverridingParser(Class)}, such a change persists, since it is
 * stored in {@link android.content.SharedPreferences}, with a key which is:
 * {@link SMSParsingManager#PREF_KEY} + {@link SMSParsingManager#instanceName}.
 * Also allows resetting to the default parsing strategy by calling
 * {@link SMSParsingManager#resetOverridingParser()}.
 *
 * @author Riccardo De Zen
 */
public class SMSParsingManager implements SMSParser {

    //Base name for Preference file name.
    static final String PREF_FILENAME = "ingsw.group1.msglibrary.parsing.SMSParsingManager";
    //Base name for Preference key. Should be followed by instance name.
    static final String PREF_KEY = "defaultParser-";
    //Map containing the active instances of this class.
    private static Map<String, SMSParsingManager> activeInstances = new ArrayMap<>();

    private WeakReference<Context> context;
    private SharedPreferences parsingPreferences;
    private String instanceName;

    /**
     * Private constructor for this class.
     *
     * @param context      The calling Context. Must not be {@code null}.
     * @param instanceName The name for this instance. Must not be {@code null}.
     */
    private SMSParsingManager(@NonNull Context context, @NonNull String instanceName) {
        this.context = new WeakReference<>(context.getApplicationContext());
        this.parsingPreferences = context.getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE);
        this.instanceName = instanceName;
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
     * @param newParser A reference to the class defining the new overriding parser.
     */
    @Nullable
    public <P extends SMSParser> Class<P> setOverridingParser(@NonNull Class<P> newParser) {
        return null;
    }

    /**
     * Method resetting the parser this instance uses to the default (no operation on messages).
     *
     * @return The reference to the previously set {@link SMSParser} class. The Type of stored
     * class is not questioned (as long as it extends {@link SMSParser}.
     */
    @Nullable
    public Class<? extends SMSParser> resetOverridingParser() {
        return null;
    }

    /**
     * @param message The outgoing {@link SMSMessage}. This is not modified, a new one is created.
     * @return The formatted {@link SMSMessage}, ready to be sent.
     */
    @Override
    public SMSMessage parseOutgoingMessage(SMSMessage message) {
        return message;
    }

    /**
     * @param message The incoming {@link SMSMessage}.
     * @return The parsed {@link SMSMessage}, as it would be before a
     * {@link SMSParsingManager#parseOutgoingMessage(SMSMessage)} call.
     */
    @Override
    public SMSMessage parseIncomingMessage(SMSMessage message) {
        return message;
    }
}
