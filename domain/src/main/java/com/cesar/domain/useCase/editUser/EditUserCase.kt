package com.cesar.domain.useCase.editUser

import android.net.Uri
import com.cesar.domain.model.User
import com.cesar.domain.repository.IRegisterRepository
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

class EditUserCase(private val repository: IRegisterRepository):IEditUserCase {
    override suspend fun execute(photo: String?, user: User): Flow<Result<String>> {
        return repository.editUser(photo,user)
    }
}