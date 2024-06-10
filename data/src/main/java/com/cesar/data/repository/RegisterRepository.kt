package com.cesar.data.repository

import android.content.Context
import com.cesar.data.datasource.remote.register.IRegisterRemoteDataSource
import com.cesar.data.util.isConnected
import com.cesar.domain.model.User
import com.cesar.domain.repository.IRegisterRepository
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class RegisterRepository (private val registerRemoteDataSource: IRegisterRemoteDataSource,private val context:Context) : IRegisterRepository {
    private val message = MutableStateFlow("")
    override suspend fun register(email: String, password: String,name:String): Flow<Result<String>> = flow{
        if (isConnected(context)){
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
        }else{
            emit(Result.Error("No cuenta con conexion a internet"))
        }

    }

    override suspend fun editUser(photo: String?, user: User): Flow<Result<String>> =  flow{
        if (isConnected(context)){
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
        }else{
            emit(Result.Error("No cuenta con conexion a internet"))
        }

    }

}