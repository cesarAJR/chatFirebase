package com.example.data.datasource.remote.login

import com.cesar.domain.model.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentReference

interface IRegisterRemoteDataSource {
    suspend fun register(email: String, password: String,name:String): AuthResult?
    suspend fun registerBD(email: String, name: String,id:String): DocumentReference?
    suspend fun validateEmailInBD(email:String): User?
}