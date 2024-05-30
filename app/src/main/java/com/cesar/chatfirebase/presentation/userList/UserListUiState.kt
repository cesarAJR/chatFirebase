package com.cesar.chatfirebase.presentation.userList

import com.cesar.domain.model.User

sealed class UserListUiState {
    data class Success(val list: List<User>?): UserListUiState()
    data class Error(val message: String): UserListUiState()
    data class SuccessLogout(val message: String): UserListUiState()
    object Loading: UserListUiState()
    object Nothing: UserListUiState()

}