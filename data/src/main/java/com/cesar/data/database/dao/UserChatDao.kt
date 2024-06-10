package com.cesar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cesar.data.database.model.UserChatEntity

@Dao
interface UserChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(users : List<UserChatEntity>)

    @Query("SELECT * FROM table_user_chat where table_user_chat.user_logged = :userId")
    fun getUserChat(userId:String) : List<UserChatEntity>

}