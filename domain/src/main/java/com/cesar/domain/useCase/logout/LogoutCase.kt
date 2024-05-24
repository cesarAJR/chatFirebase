package com.cesar.domain.useCase.logout

import com.cesar.domain.repository.IUserRepository
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

class LogoutCase(val repository: IUserRepository) : ILogoutCase{
    override suspend fun execute(): Flow<Result<String>> {
        return repository.logout()
    }
}