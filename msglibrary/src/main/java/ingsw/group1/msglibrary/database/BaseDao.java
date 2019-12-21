package ingsw.group1.msglibrary.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

/**
 * Base class for a Dao accessing a table in a Room database.
 * Any class extending this should be abstract and Override {@link #getTableName()}.
 * Overriding anything else is unnecessary for full functionality.
 * @author Riccardo De Zen
 * @param <T> The Type of Entity the Dao provides access to.
 */
@Dao
abstract class BaseDao<T>{

    //Table name has to follow
    protected static final String COUNT_QUERY = "SELECT COUNT(*) FROM ";

    //Table name has to follow
    protected static final String GET_ALL_QUERY = "SELECT * FROM ";

    /**
     * Default Query for entity insertion
     * @param entity the entities to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(T... entity);

    /**
     * Default Query for deletion of existing entities
     * @param entity the entities to remove
     */
    @Delete
    public abstract void delete(T... entity);

    /**
     * @return the number of rows in the table
     */
    public int count(){
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
            COUNT_QUERY + getTableName()
        );
        return performCount(query);
    }

    /**
     * Method to perform the query correctly through Room
     * @param query the query to be performed
     * @return an int value returned by the query. In this case the number of rows in the table.
     */
    @RawQuery
    protected abstract int performCount(SupportSQLiteQuery query);

    /**
     * @return all the rows in the table
     */
    public List<T> getAll(){
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                GET_ALL_QUERY + getTableName()
        );
        return performGetAll(query);
    }

    /**
     * Method to perform the query correctly through Room
     * @param query the query to be performed
     * @return a List returned by the query. In this case all the rows in the table.
     */
    @RawQuery
    protected abstract List<T> performGetAll(SupportSQLiteQuery query);

    /**
     * Method to be overridden in order to find the name of the table.
     * @return a String containing the name for the table this Dao provide access to.
     */
    public abstract String getTableName();
}