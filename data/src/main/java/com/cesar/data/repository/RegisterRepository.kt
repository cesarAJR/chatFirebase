package com.example.prueba_softtek.data.repository

import android.util.Log
import com.cesar.domain.model.User
import com.cesar.domain.repository.IRegisterRepository
import com.cesar.domain.repository.IUserRepository
import com.example.data.datasource.remote.login.ILoginRemoteDataSource
import com.example.data.datasource.remote.login.IRegisterRemoteDataSource
import com.example.data.datasource.remote.login.RegisterRemoteDataSource
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RegisterRepository (private val registerRemoteDataSource: IRegisterRemoteDataSource) : IRegisterRepository {

    override suspend fun register(email: String, password: String,name:String): Flow<Result<String>> = flow{
        val user = registerRemoteDataSource.validateEmailInBD(email)
        if (user == null){
            try {
                val result = registerRemoteDataSource.register(email,password,name)
                result!!.user
                emit(Result.Successfull("Se registro correctamente"))
            }catch (e:Exception){
                emit(Result.Error(e.message?:""))
            }
        }else{
            emit(Result.Error("El email ya fue registrado"))
        }
    }

}