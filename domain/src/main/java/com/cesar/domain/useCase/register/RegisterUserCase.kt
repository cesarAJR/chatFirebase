package com.cesar.domain.useCase.register

import com.cesar.domain.repository.IRegisterRepository
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class RegisterUserCase(val repository: IRegisterRepository) : IRegisterUserCase {
    override suspend fun execute(email:String, password:String,name:String): Flow<Result<String>>{
        return repository.register(email,password,name)
    }
}