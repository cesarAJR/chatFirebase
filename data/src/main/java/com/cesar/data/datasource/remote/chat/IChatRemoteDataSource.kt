package com.cesar.data.datasource.remote.chat

import com.cesar.domain.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface IChatRemoteDataSource {
    suspend fun sendMessage( toUserId: String,message:String,isConnected:Boolean,flow: MutableStateFlow<Message>):StateFlow<Message>
    suspend fun getMessage(toUserId:String,isConnected: Boolean,flow:MutableStateFlow<Message>): StateFlow<Message>
    suspend fun getListMessage(toUserId:String): MutableList<Message>?

    suspend fun getOnlineByUser(userId: String, flow: MutableStateFlow<String>): StateFlow<String>

    suspend fun sendPendingMessage(message:Message, flow: MutableStateFlow<Message>):StateFlow<Message>
}