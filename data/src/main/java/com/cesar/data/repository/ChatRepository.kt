package com.cesar.data.repository

import android.util.Log
import com.cesar.data.datasource.remote.chat.IChatRemoteDataSource
import com.cesar.domain.model.Message
import com.cesar.domain.repository.IChatRepository
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class ChatRepository(private val chatRemoteDataSource: IChatRemoteDataSource)  : IChatRepository{
    private val message = MutableStateFlow(Message())
    private val online = MutableStateFlow("")

    override suspend fun sendMessage(
        fromUserId: String,
        toUserId: String,
        message: String
    ): Flow<Result<String>> = flow{
        chatRemoteDataSource.sendMessage(fromUserId,toUserId,message)
        emit(Result.Successfull("Se registro correctamente"))
    }

    override suspend fun getMessage(
        toUserId: String
    ): Flow<Result<Message>> = flow{
         chatRemoteDataSource.getMessage(toUserId,message)
        message.collect {
            emit(Result.Successfull(it))
        }
    }

    override suspend fun getListMessage(toUserId: String): Flow<Result<MutableList<Message>>> =flow {
       val result= chatRemoteDataSource.getListMessage(toUserId)
        emit(Result.Successfull(result))
    }

    override suspend fun getOnlineByUser(
        userId: String
    ): Flow<Result<String>> = flow{
        chatRemoteDataSource.getOnlineByUser(userId,online)
            online.collect {
                if (it.isNotEmpty()){
                    emit(Result.Successfull(it))
                }
            }
    }
}