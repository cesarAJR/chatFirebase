package com.cesar.data.datasource.local.userList

import android.content.SharedPreferences
import com.cesar.data.database.dao.UserChatDao
import com.cesar.data.database.model.toListLocalUser
import com.cesar.data.database.model.toListUser
import com.cesar.domain.model.Message
import com.cesar.domain.model.User
import com.google.gson.Gson

class UserListLocalDataSource(private val dao: UserChatDao,private val sharedPreferences: SharedPreferences) : IUserListLocalDataSource {
    override fun saveUsers(users: MutableList<User>) {
        val userId = Gson().fromJson(sharedPreferences.getString("USER",""), User::class.java).id
        dao.insert(users.toListLocalUser(userId))
    }

    override suspend fun getUsers(): List<User> {
        val userId = Gson().fromJson(sharedPreferences.getString("USER",""), User::class.java).id
       return  dao.getUserChat(userId?:"").toListUser()
    }
}