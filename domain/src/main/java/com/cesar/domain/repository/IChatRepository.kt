package com.cesar.domain.repository

import com.cesar.domain.model.Message
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

interface IChatRepository {

    suspend fun sendMessage(fromUserId:String,toUserId:String,message:String): Flow<Result<String>>
    suspend fun getMessage(toUserId:String): Flow<Result<Message>>
    suspend fun getListMessage(toUserId:String): Flow<Result<MutableList<Message>>>
    suspend fun getOnlineByUser(userId: String): Flow<Result<String>>

}