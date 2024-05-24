package com.cesar.domain.useCase.userList

import com.cesar.domain.model.User
import com.cesar.domain.repository.IUserListRepository
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

class UserListCase(val repository: IUserListRepository) : IUserListCase {
    override suspend fun execute(): Flow<Result<List<User>>> {
       return repository.getUserList()
    }
}