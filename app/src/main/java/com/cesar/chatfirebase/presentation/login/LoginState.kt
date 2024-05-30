package com.cesar.chatfirebase.presentation.login

data class LoginState (
    val error:String?=null,
    val successfull:String?=null,
    val loading:Boolean?=null
)