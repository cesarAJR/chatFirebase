package com.cesar.domain.useCase.logout

import com.cesar.domain.model.User
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

interface ILogoutCase {
    suspend fun execute(): Flow<Result<String>>
}