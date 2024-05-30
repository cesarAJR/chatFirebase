package com.cesar.chatfirebase.presentation.login

import com.cesar.domain.model.User

sealed class LoginUiState {
    data class Success(val message: String?): LoginUiState()
    data class SuccessLoginGoogle(val message: String?): LoginUiState()
    data class Error(val message: String): LoginUiState()
    data class ErrorLoginGoogle(val message: String): LoginUiState()
    object Loading: LoginUiState()
    object Nothing: LoginUiState()

}