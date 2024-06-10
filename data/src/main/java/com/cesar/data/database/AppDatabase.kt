package com.cesar.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cesar.data.database.dao.MessageDataDao
import com.cesar.data.database.dao.UserChatDao
import com.cesar.data.database.model.MessageDataEntity
import com.cesar.data.database.model.UserChatEntity

@Database(entities = [UserChatEntity::class,MessageDataEntity::class], version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun UserChatDao():UserChatDao
    abstract fun MessageDataDao():MessageDataDao
}