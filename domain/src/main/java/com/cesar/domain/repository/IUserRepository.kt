package com.cesar.domain.repository

import com.cesar.domain.model.User
import kotlinx.coroutines.flow.Flow
import com.example.domain.core.Result

interface IUserRepository {

    suspend fun login(email:String,password:String):Flow<Result<User>>
    suspend fun logout():Flow<Result<String>>
    suspend fun loginGoogle(token:String):Flow<Result<User>>

}