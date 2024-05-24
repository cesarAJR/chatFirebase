package com.cesar.domain.repository

import com.cesar.domain.model.User
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

interface IUserListRepository {
    suspend fun getUserList(): Flow<Result<List<User>>>
}