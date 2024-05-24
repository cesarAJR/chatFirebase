package com.cesar.chatfirebase.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesar.chatfirebase.ui.login.LoginElements
import com.cesar.chatfirebase.ui.login.LoginUiState
import com.cesar.domain.useCase.loginGoogle.ILoginGoogleCase
import com.example.domain.useCase.login.ILoginUserCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val userLoginUserCase: ILoginUserCase,private val loginGoogleCase: ILoginGoogleCase):ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Nothing)
    val uiState: StateFlow<LoginUiState> = _uiState

    var stateElements by mutableStateOf(LoginElements())

    fun changeUser(user:String){
        stateElements= stateElements.copy(user=user)
    }

    fun changePassword(password:String){
        stateElements= stateElements.copy(password = password)
    }

    fun login(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = LoginUiState.Loading
            userLoginUserCase.execute(stateElements.user,stateElements.password)
                .collect { r ->
                    if (r.message!=null) _uiState.value = LoginUiState.Error(r.message!!)
                    else _uiState.value = LoginUiState.Success(r.data)
                }
        }
    }

    fun loginGoogle(token:String){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = LoginUiState.Loading
            loginGoogleCase.execute(token)
                .collect { r ->
                    if (r.message!=null) _uiState.value = LoginUiState.ErrorLoginGoogle(r.message!!)
                    else _uiState.value = LoginUiState.SuccessLoginGoogle(r.data)
                }
        }
    }

    fun cleanData(){
        stateElements= stateElements.copy(password = "",user="")
    }
}