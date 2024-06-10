package com.cesar.data.datasource.local.chat

import com.cesar.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface IChatLocalDataSource {

    fun saveListMessages(toUserId:String,messages : MutableList<Message>)
    fun saveMessage(toUserId:String,messages : Message)
    fun updateMessage(message : Message)
    suspend fun getListMessages(toUserId:String): Flow<List<Message>>
    suspend fun getListPendingMessages(userId:String): List<Message>

}