package com.cesar.chatfirebase.presentation.editUser

import com.cesar.domain.model.User

sealed class EditUserUiState {
    data class Success(val message: String?): EditUserUiState()
    data class Error(val message: String): EditUserUiState()
    object Loading: EditUserUiState()
    object Nothing: EditUserUiState()

}