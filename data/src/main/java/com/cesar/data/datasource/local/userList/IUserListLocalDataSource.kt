package com.cesar.data.datasource.local.userList

import com.cesar.domain.model.Message
import com.cesar.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserListLocalDataSource {

    fun saveUsers(users : MutableList<User>)
    suspend fun getUsers(): List<User>

}