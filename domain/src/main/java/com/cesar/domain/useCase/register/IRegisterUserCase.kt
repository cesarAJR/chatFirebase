package com.cesar.domain.useCase.register

import com.cesar.domain.model.User
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

interface IRegisterUserCase {
    suspend fun execute(email:String, password:String,name:String): Flow<Result<String>>
}