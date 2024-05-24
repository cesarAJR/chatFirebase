package com.cesar.domain.useCase.userList

import com.cesar.domain.model.User
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

interface IUserListCase {
    suspend fun execute(): Flow<Result<List<User>>>
}