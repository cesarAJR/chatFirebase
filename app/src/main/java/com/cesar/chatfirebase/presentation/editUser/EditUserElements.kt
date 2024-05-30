package com.cesar.chatfirebase.presentation.editUser

import android.net.Uri

data class EditUserElements(
    val photoUri : Uri?=null,
    val name : String?=null,
    val photoPath : String?=null,
)