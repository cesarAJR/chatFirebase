package com.cesar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cesar.data.database.model.MessageDataEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface MessageDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(messages : List<MessageDataEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message : MessageDataEntity)

    @Update(entity = MessageDataEntity::class)
    fun update(message : MessageDataEntity)

    @Query("SELECT * FROM table_message " +
            "WHERE chat_id_local= :chatId " +
            "AND user_logged= :userLogged " +
            "ORDER BY date_time ASC"
    )
    fun getMessages(chatId:String,userLogged:String): Flow<List<MessageDataEntity>>

    @Query("SELECT * FROM table_message " +
            "WHERE message_id= :messageId " +
            "AND user_logged= :userLogged "
    )
    fun getMessage(messageId:String,userLogged:String): List<MessageDataEntity>


    @Query("SELECT * FROM table_message " +
            "WHERE pending_sync = 1 " +
            "AND user_logged= :userLogged "
    )
    fun getMessagesPending(userLogged:String): List<MessageDataEntity>


}