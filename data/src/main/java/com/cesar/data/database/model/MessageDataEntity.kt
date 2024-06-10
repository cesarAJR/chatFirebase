package com.cesar.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cesar.domain.model.Message

@Entity(tableName = "table_message")
data class MessageDataEntity(
    @PrimaryKey
    @ColumnInfo(name = "message_id")
    val messageId :String,
    @ColumnInfo(name = "chat_id_local")
    val chatIdLocal :String,
    @ColumnInfo(name = "chat_id")
    val chatId :String,
    @ColumnInfo(name = "date_time")
    val dateTime :String,
    @ColumnInfo(name = "from_user_id")
    val fromUserId :String,
    @ColumnInfo(name = "hour")
    val hour :String,
    @ColumnInfo(name = "message")
    val message :String,
    @ColumnInfo(name = "to_user_id")
    val toUserId :String,
    @ColumnInfo(name = "user_logged")
    val userLogged :String,
    @ColumnInfo(name = "pending_sync")
    val pendingSync :Boolean,
)

fun List<Message>.toListLocalMessage(chatId: String,userLogger:String?) : List<MessageDataEntity> = map {
    MessageDataEntity(
       messageId = it.messageId?:"",
       chatIdLocal = chatId,
       chatId = it.chatId?:"",
       dateTime = it.dateTime?:"",
       fromUserId = it.fromUserId?:"",
       toUserId = it.toUserId?:"",
       hour = it.hour?:"",
       message = it.message?:"",
       userLogged = userLogger?:"",
       pendingSync = it.pendingSync?:false
    )
}

fun List<MessageDataEntity>.toListMessage() : List<Message> = map {
    Message(
       message = it.message,
       fromUserId = it.fromUserId,
       toUserId = it.toUserId,
       hour  = it.hour,
       messageId  = it.messageId,
       dateTime  = it.dateTime,
        pendingSync = it.pendingSync
    )
}

fun Message.toLocalMessage(chatId: String,userLogger:String?) : MessageDataEntity  {
  return  MessageDataEntity(
        messageId = this.messageId?:"",
      chatIdLocal = chatId,
      chatId=this.chatId?:"",
        dateTime = this.dateTime?:"",
        fromUserId = this.fromUserId?:"",
        toUserId = this.toUserId?:"",
        hour = this.hour?:"",
        message = this.message?:"",
        userLogged = userLogger?:"",
      pendingSync = this.pendingSync?:false
    )
}
