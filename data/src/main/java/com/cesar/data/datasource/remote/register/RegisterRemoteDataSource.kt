package com.example.data.datasource.remote.login

import android.content.Context
import android.util.Log
import com.cesar.data.remote.model.toUserList
import com.cesar.domain.model.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RegisterRemoteDataSource(private val auth: FirebaseAuth,private val firebaseBD: FirebaseFirestore):IRegisterRemoteDataSource {

    override suspend fun register(email: String, password: String,name:String): AuthResult? {
        val result = auth.createUserWithEmailAndPassword(email,password).await()
        if (result!=null){
            try {
                if (registerBD(email,name,result.user!!.uid)!=null){
                    return result
                }
                return null
            }catch (e:Exception){
                return null
            }
        }
        return null
    }

    override suspend fun registerBD(email: String, name: String, id: String): DocumentReference? {
        val user = hashMapOf(
            "email" to email,
            "id" to id,
            "name" to name,
        )
       return firebaseBD.collection("users").add(user).await()
    }

    override suspend fun validateEmailInBD(email: String): User? {
        val docRef = firebaseBD.collection("users")
            .whereEqualTo("email",email)
            .get().await()

        return if (docRef!=null && docRef.size()==0){
            null
        }else if(docRef==null){
            null
        }else{
            val userBD = docRef.toUserList()[0]
            userBD
        }
    }

}