package com.example.prueba_softtek.data.repository

import android.net.Uri
import android.util.Log
import com.cesar.domain.model.Message
import com.cesar.domain.model.User
import com.cesar.domain.repository.IRegisterRepository
import com.cesar.domain.repository.IUserRepository
import com.example.data.datasource.remote.login.ILoginRemoteDataSource
import com.example.data.datasource.remote.login.IRegisterRemoteDataSource
import com.example.data.datasource.remote.login.RegisterRemoteDataSource
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class RegisterRepository (private val registerRemoteDataSource: IRegisterRemoteDataSource) : IRegisterRepository {
    private val message = MutableStateFlow("")
    override suspend fun register(email: String, password: String,name:String): Flow<Result<String>> = flow{
        val user = registerRemoteDataSource.validateEmailInBD(email)
        if (user == null){
            try {
                val result = registerRemoteDataSource.register(email,password,name)
                result.collect{
                    emit(it)
                }
            }catch (e:Exception){
                emit(Result.Error("hubo un error"))
            }
        }else{
            emit(Result.Error("El email ya fue registrado"))
        }
    }

    override suspend fun editUser(photo: String?, user: User): Flow<Result<String>> =  flow{
        if (photo!=null){
            registerRemoteDataSource.uploadPhoto(photo,message)
            message.collect{
                 if (it.equals("0")){
                    emit(Result.Error("Hubo un error"))
                }else if (it.isNotEmpty()){
                    user.photoUrl = it
                     registerRemoteDataSource.editUser(user,message)
                     message.collect{
                         if (it.equals("1")){
                             emit(Result.Successfull("Se guardo correctamente"))
                         }else if (it.equals("0")){
                             emit(Result.Error("Hubo un error"))
                         }
                     }
                 }
            }
        }else{
            registerRemoteDataSource.editUser(user,message)
            message.collect{
                if (it.equals("1")){
                    emit(Result.Successfull("Se guardo correctamente"))
                }else if (it.equals("0")){
                    emit(Result.Error("Hubo un error"))
                }
            }
        }
    }

}