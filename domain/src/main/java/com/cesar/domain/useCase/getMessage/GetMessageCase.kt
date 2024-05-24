package com.cesar.domain.useCase.getMessage

import com.cesar.domain.model.Message
import com.cesar.domain.repository.IChatRepository
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

class GetMessageCase(val repository: IChatRepository) : IGetMessageCase {
    override suspend fun execute(toUserId:String): Flow<Result<Message>>{
        return repository.getMessage(toUserId)
    }
}