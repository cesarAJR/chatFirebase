package com.cesar.data.repository

import android.content.Context
import com.cesar.data.datasource.local.chat.IChatLocalDataSource
import com.cesar.data.datasource.remote.chat.IChatRemoteDataSource
import com.cesar.data.util.isConnected
import com.cesar.domain.model.Message
import com.cesar.domain.repository.IChatRepository
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class ChatRepository(private val chatRemoteDataSource: IChatRemoteDataSource,private val chatLocalDataSource: IChatLocalDataSource,private val context:Context)  : IChatRepository{
    private val message = MutableStateFlow(Message())
    private val online = MutableStateFlow("")
    private val sendMessage = MutableStateFlow(Message())
    private val sendMessagePending = MutableStateFlow(Message())

    override suspend fun sendMessage(
        fromUserId: String,
        toUserId: String,
        message: String
    ): Flow<Result<String>> = flow{
        chatRemoteDataSource.sendMessage(toUserId,message, isConnected(context),sendMessage)
        sendMessage.collect{
            if (it.message?.isNotEmpty()==true){
                chatLocalDataSource.saveMessage(toUserId,it)
                emit(Result.Successfull("Se registro correctamente"))
            }
        }
    }

    override suspend fun getMessage(
        toUserId: String
    ): Flow<Result<Message>> = flow{
         chatRemoteDataSource.getMessage(toUserId,isConnected(context),message)
         message.collect {
            if (it.message?.isNotEmpty()==true){
                chatLocalDataSource.saveMessage(toUserId,it)
            }
            emit(Result.Successfull(it))
        }
    }

    override suspend fun getListMessage(toUserId: String): Flow<Result<MutableList<Message>>> =flow {
        if (isConnected(context)){
            val result= chatRemoteDataSource.getListMessage(toUserId)
            if (result?.isNotEmpty()==true) {
                chatLocalDataSource.saveListMessages(toUserId,result)
            }
            emit(Result.Successfull(mutableListOf()))
        }else{
            emit(Result.Successfull(mutableListOf()))
        }
    }

    override suspend fun getOnlineByUser(
        userId: String
    ): Flow<Result<String>> = flow{
        if (isConnected(context)){
            chatRemoteDataSource.getOnlineByUser(userId,online)
            online.collect {
                if (it.isNotEmpty()){
                    emit(Result.Successfull(it))
                }
            }
        }
    }

     override suspend fun getListMessageLocal(toUserId: String): Flow<Result<MutableList<Message>>> =flow {
         chatLocalDataSource.getListMessages(toUserId).collect{ it ->
             emit(Result.Successfull(it.toMutableList()))
         }
    }

    override suspend fun getListPendingMessage(userId:String): List<Message> {
       return chatLocalDataSource.getListPendingMessages(userId)
    }

    override suspend fun sendPendingMessage(messages: List<Message>): Flow<Result<String>> = flow{
        if (isConnected(context)){
            messages.forEach {message->
                chatRemoteDataSource.sendPendingMessage(message,sendMessagePending)
            }
        }
    }
}