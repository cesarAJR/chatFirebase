package com.example.prueba_softtek.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.cesar.domain.model.User
import com.cesar.domain.repository.IUserRepository
import com.example.data.datasource.remote.login.ILoginRemoteDataSource
import com.example.data.datasource.remote.login.IRegisterRemoteDataSource
import com.example.domain.core.Result
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class UserRepository (private val loginRemoteDataSource: ILoginRemoteDataSource,
                      private val registerRemoteDataSource: IRegisterRemoteDataSource,
                      private val sharedPreferences: SharedPreferences
) : IUserRepository {
    private val message = MutableStateFlow("")
    override suspend fun login(email: String, password: String): Flow<Result<String>> = flow{
        try {
            loginRemoteDataSource.login(email,password)
            val user = registerRemoteDataSource.validateEmailInBD(email)
            sharedPreferences.edit().putString("USER",Gson().toJson(user)).apply()
            emit(Result.Successfull("login exitoso"))
        }catch (e:Exception){
            emit(Result.Error(e.message?:""))
        }
    }

    override suspend fun logout(): Flow<Result<String>> = flow{
        val result = loginRemoteDataSource.logout()
        if (result.equals("1")){
            sharedPreferences.edit().putString("USER",null).apply()
            emit(Result.Successfull("Se cerro sesión"))
        }else{
            emit(Result.Error("Hubo un error"))
        }
    }

    override suspend fun loginGoogle(token: String): Flow<Result<String>> = flow{
        try {
            val result = loginRemoteDataSource.loginGoogle(token)
            val user = registerRemoteDataSource.validateEmailInBD(result!!.user?.email?:"")
            if (user==null){
                val email = result.user?.email?:""
                val name = result.user?.displayName?:""
                registerRemoteDataSource.registerBD(email,name,result.user?.uid?:"",message)
                val user = registerRemoteDataSource.validateEmailInBD(result.user?.email?:"")
                sharedPreferences.edit().putString("USER",Gson().toJson(user)).apply()
                emit(Result.Successfull("login exitoso"))
            }else{
                sharedPreferences.edit().putString("USER", Gson().toJson(user)).apply()
                emit(Result.Successfull("login exitoso"))
            }
        }catch (e:Exception){
            loginRemoteDataSource.logout()
            sharedPreferences.edit().putString("USER",null).apply()
            emit(Result.Error(e.message?:""))
        }
    }
}