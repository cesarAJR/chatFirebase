package com.cesar.data.remote.model

import com.cesar.domain.model.Message
import com.cesar.domain.model.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

fun QuerySnapshot.toUserList() :List<User> = map {
    User(
        id = it.data.get("id").toString(),
        email = it.data.get("email").toString(),
        name = it.data.get("name").toString(),
        photoUrl = it.data.get("photoUrl").toString(),
        online = it.data.get("online").toString().toBoolean(),
    )
}

fun List<DocumentSnapshot>.toMessageList() :List<Message> = map {
    Message(
        message = it.data?.get("message").toString(),
        fromUserId = it.data?.get("fromUserId").toString(),
        toUserId = it.data?.get("toUserId").toString(),
        hour = it.data?.get("hour").toString(),
        messageId = it.data?.get("messageId").toString(),
        dateTime  = it.data?.get("dateTime").toString(),
    )
}

fun QueryDocumentSnapshot.toMessage() :Message  {
   return Message(
       message = data["message"].toString(),
       fromUserId = data["fromUserId"].toString(),
       toUserId = data["toUserId"].toString(),
       hour = data["hour"].toString(),
       messageId = data["messageId"].toString(),
       dateTime = data["dateTime"].toString(),
       pendingSync = false,
       chatId = data["chatId"].toString()
   )
}