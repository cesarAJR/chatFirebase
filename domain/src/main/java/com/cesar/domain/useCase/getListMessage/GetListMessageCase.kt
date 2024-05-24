package com.cesar.domain.useCase.getListMessage

import com.cesar.domain.model.Message
import com.cesar.domain.repository.IChatRepository
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

class GetListMessageCase(val repository: IChatRepository) : IGetListMessageCase {
    override suspend fun execute(toUserId:String): Flow<Result<MutableList<Message>>>{
        return repository.getListMessage(toUserId)
    }
}