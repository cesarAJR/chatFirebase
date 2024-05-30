package com.cesar.chatfirebase.viewModel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesar.chatfirebase.ui.chat.ChatUiState
import com.cesar.chatfirebase.ui.editUser.EditUserElements
import com.cesar.chatfirebase.ui.editUser.EditUserUiState
import com.cesar.domain.model.User
import com.cesar.domain.useCase.editUser.IEditUserCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditUserViewModel(private val editUserCase: IEditUserCase): ViewModel() {
    private val _uiState = MutableStateFlow<EditUserUiState>(EditUserUiState.Nothing)
    val uiState: StateFlow<EditUserUiState> = _uiState


    var stateElements by mutableStateOf(EditUserElements())
    var user: User?=null

    fun changeData(photoUri: Uri?, name:String){
        var userName = name
        if (stateElements.name!=null){
            userName = stateElements.name!!
        }
        stateElements = stateElements.copy(photoUri = photoUri,name=userName)
    }
    fun changePhotoUri(photoUri: Uri,photoPath:String?){
        stateElements = stateElements.copy(photoUri = photoUri, photoPath = photoPath)
    }

    fun changeName(name:String){
        stateElements= stateElements.copy(name=name)
    }

    fun changeUser(user:User){
        this.user = user
    }

    fun editUser(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = EditUserUiState.Loading
            user?.name= stateElements.name
            editUserCase.execute(stateElements.photoPath,user!!)
                .collect { r ->
                    if (r.message!=null) _uiState.value = EditUserUiState.Error(r.message!!)
                    else _uiState.value = EditUserUiState.Success(r.data)
                }
        }
    }
}