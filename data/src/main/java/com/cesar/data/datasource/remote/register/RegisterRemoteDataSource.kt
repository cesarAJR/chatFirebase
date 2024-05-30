package com.example.data.datasource.remote.login

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.cesar.data.remote.model.toUserList
import com.cesar.domain.model.Message
import com.cesar.domain.model.User
import com.example.domain.core.Result
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.File

class RegisterRemoteDataSource(private val auth: FirebaseAuth,private val firebaseBD: FirebaseFirestore,private val firebaseStorage: FirebaseStorage,private val sharedPreferences: SharedPreferences):IRegisterRemoteDataSource {
    private val message = MutableStateFlow("")
    override suspend fun register(email: String, password: String,name:String): Flow<Result<String>> = flow {
        val result = auth.createUserWithEmailAndPassword(email,password).await()
        if (result!=null){
            try {
                registerBD(email,name,result.user!!.uid,message)
                message.collect{
                    if (message.value == "1"){
                        emit(Result.Successfull("bien"))
                    }else if (message.value == "0"){
                        emit(Result.Error("error"))
                    }
                }
            }catch (e:Exception){
                emit(Result.Error("error"))
            }
        }
        emit(Result.Error("error"))
    }

    override suspend fun
            registerBD(
        email: String,
        name: String,
        id: String,
        flow: MutableStateFlow<String>
    ): StateFlow<String> {
        val user = hashMapOf(
            "email" to email,
            "id" to id,
            "name" to name,
            "online" to false
        )

        firebaseBD.collection("users").document(id).set(user)
        .addOnSuccessListener {
            flow.value = "1"
        }.addOnFailureListener {
                flow.value = "0"
        }

        return flow
    }

    override suspend fun editUser(user: User, flow: MutableStateFlow<String>): StateFlow<String> {
            firebaseBD.collection("users").document(user.id!!).set(user)
                .addOnSuccessListener {
                    sharedPreferences.edit().putString("USER", Gson().toJson(user)).apply()
                flow.value="1"
            }.addOnFailureListener {
                    flow.value="0"
                }
        return flow
    }

    override suspend fun uploadPhoto(photoPath: String,flow: MutableStateFlow<String>): StateFlow<String> {
        val storageRef = firebaseStorage.reference
        val file = Uri.fromFile(File(photoPath))
        val riversRef = storageRef.child("images/user_profile/${file.lastPathSegment}")
        val uploadTask = riversRef.putFile(file)
       uploadTask.addOnSuccessListener {
           riversRef.downloadUrl.addOnCompleteListener {task->
               flow.value= task.result.toString()
           }
        }.addOnFailureListener {
            flow.value="0"
        }
        return  flow
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