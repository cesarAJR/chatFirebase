package com.example.domain.useCase.login

import com.cesar.domain.model.User
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

interface ILoginUserCase {
    suspend fun execute(email:String, password:String): Flow<Result<String>>
}