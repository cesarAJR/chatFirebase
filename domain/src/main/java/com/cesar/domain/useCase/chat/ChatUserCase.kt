package com.example.domain.useCase.login

import com.cesar.domain.repository.IChatRepository
import com.cesar.domain.useCase.chat.IChatUserCase
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

class ChatUserCase(val repository: IChatRepository) : IChatUserCase {
    override suspend fun execute(
        fromUserId: String,
        toUserId: String,
        message: String
    ): Flow<Result<String>> {
       return repository.sendMessage(fromUserId,toUserId,message)
    }
}