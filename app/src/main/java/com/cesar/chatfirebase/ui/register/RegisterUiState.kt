package com.cesar.chatfirebase.ui.register

import com.cesar.domain.model.User

sealed class RegisterUiState {
    data class Success(val message: String?): RegisterUiState()
    data class SuccessLogin(val message: String?): RegisterUiState()
    data class Error(val message: String): RegisterUiState()
    object Loading: RegisterUiState()
    object Nothing: RegisterUiState()
}