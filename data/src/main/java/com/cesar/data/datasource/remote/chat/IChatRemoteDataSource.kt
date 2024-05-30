package com.cesar.data.datasource.remote.chat

import com.cesar.domain.model.Message
import com.example.domain.core.Result
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface IChatRemoteDataSource {
    suspend fun sendMessage(fromUserId: String, toUserId: String,message:String):String
    suspend fun getMessage(toUserId:String,flow:MutableStateFlow<Message>): StateFlow<Message>
    suspend fun getListMessage(toUserId:String): MutableList<Message>?

    suspend fun getOnlineByUser(userId: String, flow: MutableStateFlow<String>): StateFlow<String>
}