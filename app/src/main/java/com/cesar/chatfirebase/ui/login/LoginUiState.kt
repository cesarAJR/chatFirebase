package com.cesar.chatfirebase.ui.login

import com.cesar.domain.model.User

sealed class LoginUiState {
    data class Success(val user: User?): LoginUiState()
    data class SuccessLoginGoogle(val user: User?): LoginUiState()
    data class Error(val message: String): LoginUiState()
    data class ErrorLoginGoogle(val message: String): LoginUiState()
    object Loading: LoginUiState()
    object Nothing: LoginUiState()

}