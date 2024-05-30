package com.cesar.domain.useCase.loginGoogle

import com.cesar.domain.model.User
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

interface ILoginGoogleCase {
    suspend fun execute(token:String): Flow<Result<String>>
}