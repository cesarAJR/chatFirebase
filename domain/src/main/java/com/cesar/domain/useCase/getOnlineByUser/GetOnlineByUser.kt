package com.cesar.domain.useCase.getOnlineByUser

import com.cesar.domain.repository.IChatRepository
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

class GetOnlineByUser(private val repository: IChatRepository) :IGetOnlineByUser {
    override suspend fun execute(userId: String): Flow<Result<String>> {
        return repository.getOnlineByUser(userId)
    }
}