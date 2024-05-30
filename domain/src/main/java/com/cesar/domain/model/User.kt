package com.cesar.domain.model

data class User(
    val id :String?=null,
    var name:String?=null,
    val email:String?=null,
    var photoUrl:String?=null,
    var online:Boolean?=null
)