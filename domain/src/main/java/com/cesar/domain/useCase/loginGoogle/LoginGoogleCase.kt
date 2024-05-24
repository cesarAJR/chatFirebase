package com.cesar.domain.useCase.loginGoogle

import com.cesar.domain.model.User
import com.cesar.domain.repository.IUserRepository
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

class LoginGoogleCase(private val repository: IUserRepository) : ILoginGoogleCase {
    override suspend fun execute(token: String): Flow<Result<User>> {
        return repository.loginGoogle(token)
    }
}