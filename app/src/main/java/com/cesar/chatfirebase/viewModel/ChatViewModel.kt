package com.cesar.chatfirebase.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesar.chatfirebase.presentation.chat.ChatElements
import com.cesar.chatfirebase.presentation.chat.ChatUiState
import com.cesar.domain.model.Message
import com.cesar.domain.useCase.chat.IChatUserCase
import com.cesar.domain.useCase.getListMessage.IGetListMessageCase
import com.cesar.domain.useCase.getMessage.IGetMessageCase
import com.cesar.domain.useCase.getOnlineByUser.IGetOnlineByUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val chatUserCase: IChatUserCase,
                    private val getMessageCase: IGetMessageCase,
                    private val getListMessageCase: IGetListMessageCase,
                    private val getOnlineByUser: IGetOnlineByUser,
):ViewModel() {
    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Nothing)
    val uiState: StateFlow<ChatUiState> = _uiState


    var stateElements by mutableStateOf(ChatElements())

    fun updateListMessages(listMessage:MutableList<Message>){
        stateElements = stateElements.copy(messages = listMessage)
    }

    fun updateMessages(messageData:Message){
        val messages : MutableList<Message> = mutableListOf()
        messages.addAll(stateElements.messages)
        var repeatMessage = false
        messages.forEach {
            if (it.messageId.equals(messageData.messageId)){
                repeatMessage = true
            }
        }
        if (!repeatMessage){
            messages.add(messageData)
            stateElements = stateElements.copy(messages = messages)
        }
    }

    fun changeMessage(message:String){
        stateElements= stateElements.copy(message = message)
    }

    fun changeUserId(fromUserId:String,toUserId:String){
        stateElements= stateElements.copy(fromUserId = fromUserId, toUserId = toUserId)
    }

    fun sendMessage(){
        if (stateElements.message.trim().isNotEmpty()){
            viewModelScope.launch(Dispatchers.IO) {
                chatUserCase.execute(stateElements.fromUserId,stateElements.toUserId,stateElements.message)
                    .collect { r ->
                        changeMessage("")
                        if (r.message!=null) _uiState.value = ChatUiState.Error(r.message!!)
                        else _uiState.value = ChatUiState.Success(r.data)
                    }
            }
        }
    }

    fun getMessage(){
        viewModelScope.launch(Dispatchers.IO) {
            getMessageCase.execute(stateElements.toUserId)
                .collect { r ->
                    if (r.message!=null) _uiState.value = ChatUiState.Error(r.message!!)
                    else {
                       _uiState.value = ChatUiState.SuccessGetMessage(r.data)
                    }
                }
        }
    }

    fun getListMessage(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = ChatUiState.Loading
            getListMessageCase.execute(stateElements.toUserId)
                .collect { r ->
                    if (r.message!=null) _uiState.value = ChatUiState.Error(r.message!!)
                    else {
                        _uiState.value = ChatUiState.SuccessGetListMessage(r.data)
                    }
                }
        }
    }

    fun getOnlineByUser(){
        viewModelScope.launch(Dispatchers.IO) {
            getOnlineByUser.execute(stateElements.toUserId)
                .collect { r ->
                    if (r.message!=null && r.message!!.isNotEmpty()) _uiState.value = ChatUiState.Nothing
                    else {
                        _uiState.value = ChatUiState.SuccessGetOnlineByUser(r.data)
                    }
                }
        }
    }
}