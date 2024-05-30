package com.example.data.datasource.remote.login

import android.net.Uri
import com.cesar.domain.model.Message
import com.cesar.domain.model.User
import com.example.domain.core.Result
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface IRegisterRemoteDataSource {
    suspend fun register(email: String, password: String,name:String): Flow<Result<String>>
    suspend fun registerBD(email: String, name: String,id:String,flow: MutableStateFlow<String>): StateFlow<String>
    suspend fun editUser(user: User,flow: MutableStateFlow<String>?): StateFlow<String>
    suspend fun uploadPhoto(photoPath: String,flow: MutableStateFlow<String>): StateFlow<String>
    suspend fun validateEmailInBD(email:String): User?
}