package com.cesar.domain.useCase.getOnlineByUser

import com.cesar.domain.model.Message
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

interface IGetOnlineByUser {
    suspend fun execute(userId:String): Flow<Result<String>>
}