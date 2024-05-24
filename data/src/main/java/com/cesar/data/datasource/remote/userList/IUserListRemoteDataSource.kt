package com.cesar.data.datasource.remote.userList

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Deferred

interface IUserListRemoteDataSource {
    suspend fun getUserList() : QuerySnapshot?
}

