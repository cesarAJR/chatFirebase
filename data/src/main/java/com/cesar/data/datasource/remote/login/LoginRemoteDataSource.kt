package com.cesar.data.datasource.remote.login

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class LoginRemoteDataSource(private val auth: FirebaseAuth):ILoginRemoteDataSource {
    override suspend fun login(email: String, password: String): AuthResult? {
            val result = auth.signInWithEmailAndPassword(email,password).await()
            return result
    }

    override suspend fun logout(): String? {
        try {
            auth.signOut()
            return "1"
        }catch (e:Exception){
            return "0"
        }
    }

    override suspend fun loginGoogle(token:String): AuthResult? {
        val credential = GoogleAuthProvider.getCredential(token, null)
        return auth.signInWithCredential(credential).await()
    }
}