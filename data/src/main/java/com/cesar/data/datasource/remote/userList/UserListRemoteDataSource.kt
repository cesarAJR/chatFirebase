package com.cesar.data.datasource.remote.userList

import android.util.Log
import com.example.data.datasource.remote.login.IRegisterRemoteDataSource
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class UserListRemoteDataSource( private val firebaseBD: FirebaseFirestore,private val auth: FirebaseAuth): IUserListRemoteDataSource {
    override suspend fun getUserList(): QuerySnapshot? {
        return firebaseBD.collection("users")
            .whereNotEqualTo("id",auth.uid)
            .get()
            .await()
    }
}