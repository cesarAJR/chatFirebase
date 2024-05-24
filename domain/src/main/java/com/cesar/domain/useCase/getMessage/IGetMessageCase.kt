package com.cesar.domain.useCase.getMessage

import com.cesar.domain.model.Message
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

interface IGetMessageCase {
    suspend fun execute(toUserId:String): Flow<Result<Message>>
}