package com.cesar.data.repository

import android.content.Context
import com.cesar.data.datasource.local.userList.IUserListLocalDataSource
import com.cesar.data.datasource.remote.userList.IUserListRemoteDataSource
import com.cesar.data.remote.model.toUserList
import com.cesar.data.util.isConnected
import com.cesar.domain.model.User
import com.cesar.domain.repository.IUserListRepository
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserListRepository(
    private val userListRemoteDataSource: IUserListRemoteDataSource,
    private val userListLocalDataSource: IUserListLocalDataSource,
    private val context:Context
) : IUserListRepository {
    override suspend fun getUserList(): Flow<Result<List<User>>> = flow {
       if (isConnected(context)){
           val result= userListRemoteDataSource.getUserList()

           if (result!=null){
               val list = result.toUserList()
               userListLocalDataSource.saveUsers(list.toMutableList())
               emit(Result.Successfull(list))
           }else{
               emit(Result.Error("error"))
           }
       }else{
          val result =  userListLocalDataSource.getUsers()
           emit(Result.Successfull(result))
       }
    }
}