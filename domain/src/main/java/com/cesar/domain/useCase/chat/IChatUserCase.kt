package com.cesar.domain.useCase.chat

import com.cesar.domain.model.User
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

interface IChatUserCase {
    suspend fun execute(fromUserId:String, toUserId:String,message:String): Flow<Result<String>>
}