package com.cesar.data.repository

import com.cesar.data.datasource.remote.userList.IUserListRemoteDataSource
import com.cesar.data.remote.model.toUserList
import com.cesar.domain.model.User
import com.cesar.domain.repository.IUserListRepository
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserListRepository(private val userListRemoteDataSource: IUserListRemoteDataSource) : IUserListRepository {
    override suspend fun getUserList(): Flow<Result<List<User>>> = flow {
       val result= userListRemoteDataSource.getUserList()

        if (result!=null){
            emit(Result.Successfull(result.toUserList()))
        }else{
            emit(Result.Error("error"))
        }
    }
}