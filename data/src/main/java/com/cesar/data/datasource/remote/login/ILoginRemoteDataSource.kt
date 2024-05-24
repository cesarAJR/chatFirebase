package com.example.data.datasource.remote.login

import com.google.firebase.auth.AuthResult

interface ILoginRemoteDataSource {
    suspend fun login(email: String, password: String): AuthResult?
    suspend fun logout(): String?
    suspend fun loginGoogle(token:String): AuthResult?
}