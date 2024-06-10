package com.cesar.domain.model

data class Message(
    val message: String?=null,
    val fromUserId: String?=null,
    val toUserId: String?=null,
    val hour: String?=null,
    val messageId: String?=null,
    val dateTime: String?=null,
    var pendingSync: Boolean?=null,
    var chatId: String?=null,
)