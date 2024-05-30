package com.cesar.domain.useCase.editUser

import android.net.Uri
import com.cesar.domain.model.Message
import com.cesar.domain.model.User
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

interface IEditUserCase {
    suspend fun execute(photo: String?,user: User): Flow<Result<String>>
}