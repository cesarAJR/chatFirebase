package com.cesar.domain.useCase.getListMessage

import com.cesar.domain.model.Message
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

interface IGetListMessageCase {
    suspend fun execute(toUserId:String): Flow<Result<MutableList<Message>>>
}