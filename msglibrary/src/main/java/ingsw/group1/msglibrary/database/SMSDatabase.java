package ingsw.group1.msglibrary.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ingsw.group1.msglibrary.SMSMessage;

/**
 * @author Riccardo De Zen
 * Abstract class to allow Room library to instantiate the database.
 */
@Database(entities = {SMSMessage.class}, version = 1)
@TypeConverters({SMSConverters.class})
public abstract class SMSDatabase extends RoomDatabase {
    public abstract SMSDao access();
}