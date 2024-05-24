package com.cesar.data.remote.model

import com.cesar.domain.model.Message
import com.cesar.domain.model.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

fun QuerySnapshot.toUserList() :List<User> = map {
    User(
        id = it.data.get("id").toString(),
        email = it.data.get("email").toString(),
        name = it.data.get("name").toString(),
    )
}

fun List<DocumentSnapshot>.toMessageList() :List<Message> = map {
    Message(
        message = it.data?.get("message").toString(),
        fromUserId = it.data?.get("fromUserId").toString(),
        toUserId = it.data?.get("toUserId").toString(),
        hour = it.data?.get("hour").toString(),
        messageId = it.data?.get("messageId").toString(),
    )
}