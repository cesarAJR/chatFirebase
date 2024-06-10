package com.cesar.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cesar.domain.model.Message
import com.cesar.domain.model.User

@Entity(tableName = "table_user_chat")
data class UserChatEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId :String,
    @ColumnInfo(name = "email")
    val email :String,
    @ColumnInfo(name = "name")
    val name :String,
    @ColumnInfo(name = "photo_url")
    val photoUrl :String,
    @ColumnInfo(name = "user_logged")
    val userLogged :String
)


fun List<User>.toListLocalUser(userLogged:String?) : List<UserChatEntity> = map {
    UserChatEntity(
        userId = it.id?:"",
        email = it.email?:"",
        name = it.name?:"",
        photoUrl = it.photoUrl?:"",
        userLogged = userLogged?:"",
    )
}

fun List<UserChatEntity>.toListUser() : List<User> = map {
    User(
        id = it.userId,
        email = it.email,
        name = it.name,
        photoUrl = it.photoUrl,
        online =false
    )
}