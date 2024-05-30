package com.cesar.chatfirebase.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesar.chatfirebase.presentation.login.LoginElements
import com.cesar.chatfirebase.presentation.userList.UserListElements
import com.cesar.chatfirebase.presentation.userList.UserListUiState
import com.cesar.domain.model.User
import com.cesar.domain.useCase.logout.ILogoutCase
import com.cesar.domain.useCase.userList.IUserListCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


class UserListViewModel(private val userListCase: IUserListCase,private val logoutCase: ILogoutCase): ViewModel() {
    var stateElements by mutableStateOf(UserListElements())

    private val _uiState = MutableStateFlow<UserListUiState>(UserListUiState.Nothing)
    val uiState: StateFlow<UserListUiState> = _uiState

    fun updateUserList(list:List<User>){
        stateElements= stateElements.copy(users = list)
    }

    fun getUserList(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UserListUiState.Loading
            userListCase.execute()
                .collect { r ->
                    if (r.message!=null) _uiState.value = UserListUiState.Error(r.message!!)
                    else _uiState.value = UserListUiState.Success(r.data)
                }
        }
    }

    fun logout(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UserListUiState.Loading
            logoutCase.execute()
                .collect { r ->
                    if (r.message!=null) _uiState.value = UserListUiState.Error(r.message!!)
                    else _uiState.value = UserListUiState.SuccessLogout(r.data!!)
                }
        }
    }

}