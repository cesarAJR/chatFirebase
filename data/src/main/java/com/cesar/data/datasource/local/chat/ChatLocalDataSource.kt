package com.cesar.data.datasource.local.chat

import android.content.SharedPreferences
import com.cesar.data.database.dao.MessageDataDao
import com.cesar.data.database.dao.UserChatDao
import com.cesar.data.database.model.MessageDataEntity
import com.cesar.data.database.model.UserChatEntity
import com.cesar.data.database.model.toListLocalMessage
import com.cesar.data.database.model.toListMessage
import com.cesar.data.database.model.toLocalMessage
import com.cesar.domain.model.Message
import com.cesar.domain.model.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ChatLocalDataSource(
    private val userChatDao: UserChatDao,
    private val messageDataDao: MessageDataDao,
    private val sharedPreferences: SharedPreferences
): IChatLocalDataSource {

      override fun saveListMessages(toUserId:String, messages : MutableList<Message>) {
          val from = Gson().fromJson(sharedPreferences.getString("USER",""), User::class.java).id
          val messageDataDb = messages.toListLocalMessage("$from-$toUserId",from)
          messageDataDao.insert(messageDataDb)
    }

    override fun saveMessage(toUserId: String, message: Message) {
        val from = Gson().fromJson(sharedPreferences.getString("USER",""), User::class.java).id
        val messageDataDb = message.toLocalMessage("$from-$toUserId",from)
        val messageLocal = messageDataDao.getMessage(message.messageId?:"",from?:"")
        if (messageLocal.isNotEmpty()){
            messageDataDao.update(messageDataDb)
        }else{
            messageDataDao.insert(messageDataDb)
        }
    }

    override fun updateMessage(message: Message) {
        val from = Gson().fromJson(sharedPreferences.getString("USER",""), User::class.java).id
        messageDataDao.update(message.toLocalMessage("${message.fromUserId}-${message.toUserId}",from))
    }

    override suspend fun getListMessages(toUserId: String): Flow<List<Message>> = flow {
        val from = Gson().fromJson(sharedPreferences.getString("USER",""), User::class.java).id
         messageDataDao.getMessages("$from-$toUserId",from?:"").map {
             it.toListMessage()
         }.collect{
             emit(it)
         }
    }

    override suspend fun getListPendingMessages(userId:String): List<Message> {
        return messageDataDao.getMessagesPending(userId).toListMessage()
    }

}