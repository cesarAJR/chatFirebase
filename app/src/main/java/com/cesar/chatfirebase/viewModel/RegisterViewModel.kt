package com.cesar.chatfirebase.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesar.chatfirebase.presentation.login.LoginElements
import com.cesar.chatfirebase.presentation.login.LoginUiState
import com.cesar.chatfirebase.presentation.register.RegisterElements
import com.cesar.chatfirebase.presentation.register.RegisterUiState
import com.cesar.domain.useCase.register.IRegisterUserCase
import com.cesar.domain.useCase.register.RegisterUserCase
import com.example.domain.useCase.login.ILoginUserCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerUserCase: IRegisterUserCase,private val loginUserCase: ILoginUserCase):ViewModel() {
    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Nothing)
    val uiState: StateFlow<RegisterUiState> = _uiState

    var stateElements by mutableStateOf(RegisterElements())

    fun changeUser(user:String){
        stateElements= stateElements.copy(user=user)
    }

    fun changeName(name:String){
        stateElements= stateElements.copy(name=name)
    }

    fun changePassword(password:String){
        stateElements= stateElements.copy(password = password)
    }

    fun register(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = RegisterUiState.Loading
            registerUserCase.execute(stateElements.user,stateElements.password,stateElements.name)
                .collect { r ->
                    if (r.message!=null) _uiState.value = RegisterUiState.Error(r.message!!)
                    else _uiState.value = RegisterUiState.Success(r.data)

                }
        }
    }

    fun login(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = RegisterUiState.Loading
            loginUserCase.execute(stateElements.user,stateElements.password)
                .collect { r ->
                    if (r.message!=null) _uiState.value = RegisterUiState.Error(r.message!!)
                    else _uiState.value = RegisterUiState.SuccessLogin(r.data)

                }
        }
    }

    fun cleanData(){
        stateElements= stateElements.copy(password = "", user = "")
    }

}