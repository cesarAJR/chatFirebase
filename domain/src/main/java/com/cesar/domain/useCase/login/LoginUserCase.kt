package com.example.domain.useCase.login

import com.cesar.domain.model.User
import com.cesar.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import com.example.domain.core.Result

class LoginUserCase(val repository: IUserRepository) : ILoginUserCase {
    override suspend fun execute(email:String, password:String): Flow<Result<String>> {
        return repository.login(email,password)
    }
}