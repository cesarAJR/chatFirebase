package com.cesar.domain.repository

import com.cesar.domain.model.User
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

interface IRegisterRepository {

    suspend fun register(email:String,password:String,name:String):Flow<Result<String>>
    suspend fun editUser(photo: String?, user:User):Flow<Result<String>>

}