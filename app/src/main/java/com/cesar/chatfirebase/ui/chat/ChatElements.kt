package com.cesar.chatfirebase.ui.chat

import com.cesar.domain.model.Message
import com.cesar.domain.model.User

data class ChatElements(
    val message:String="",
    val fromUserId:String="",
    val toUserId:String="",
    val messages: MutableList<Message> =  mutableListOf(),
)