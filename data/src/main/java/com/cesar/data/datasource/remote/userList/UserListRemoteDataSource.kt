package com.cesar.data.datasource.remote.userList

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