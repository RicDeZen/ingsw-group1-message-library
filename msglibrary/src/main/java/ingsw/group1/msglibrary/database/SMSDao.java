package ingsw.group1.msglibrary.database;

import androidx.room.Dao;

import ingsw.group1.msglibrary.SMSMessage;

/**
 * @author Riccardo De Zen
 * Interface extending the BaseDao class for SMSMessage
 */
@Dao
abstract class SMSDao extends BaseDao<SMSMessage>{
    /**
     * @return the name of the table containing the SMSMessage entities.
     */
    @Override
    public String getTableName(){
        return SMSMessage.SMS_TABLE_NAME;
    }
}