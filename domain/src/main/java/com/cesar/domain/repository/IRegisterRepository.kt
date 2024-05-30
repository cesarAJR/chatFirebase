package com.cesar.domain.repository

import android.net.Uri
import com.cesar.domain.model.User
import kotlinx.coroutines.flow.Flow
import com.example.domain.core.Result

interface IRegisterRepository {

    suspend fun register(email:String,password:String,name:String):Flow<Result<String>>
    suspend fun editUser(photo: String?, user:User):Flow<Result<String>>

}