package com.cesar.chatfirebase.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesar.chatfirebase.presentation.chat.ChatElements
import com.cesar.chatfirebase.presentation.chat.GetOnlineState
import com.cesar.chatfirebase.presentation.chat.ChatUiState
import com.cesar.domain.model.Message
import com.cesar.domain.useCase.chat.IChatUserCase
import com.cesar.domain.useCase.getListMessage.IGetListMessageCase
import com.cesar.domain.useCase.getListMessage.IGetListMessageLocalCase
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
                    private val getListMessageLocalCase: IGetListMessageLocalCase
):ViewModel() {

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Nothing)
    val uiState: StateFlow<ChatUiState> = _uiState

    private val _ui2State = MutableStateFlow<GetOnlineState>(GetOnlineState.Nothing)
    val ui2State: StateFlow<GetOnlineState> = _ui2State


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
                    }
            }
        }
    }

    fun getMessage(){
        viewModelScope.launch(Dispatchers.IO) {
            getMessageCase.execute(stateElements.toUserId)
                .collect { r ->
                }
        }
    }

    fun getListMessage(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = ChatUiState.Loading
            getListMessageCase.execute(stateElements.toUserId)
                .collect { r ->
                    _uiState.value = ChatUiState.SuccessGetListMessage
                }
        }
    }

    fun getListMessageLocal(){
        viewModelScope.launch(Dispatchers.IO) {
            getListMessageLocalCase.execute(stateElements.toUserId)
                .collect { r ->
                    if (r.message!=null) _uiState.value = ChatUiState.Error(r.message!!)
                    else {
                        _uiState.value = ChatUiState.SuccessGetListMessageLocal(r.data)
                    }
                }
        }
    }

    fun getOnlineByUser(){
        viewModelScope.launch(Dispatchers.IO) {
            getOnlineByUser.execute(stateElements.toUserId)
                .collect { r ->
                    if (r.message!=null && r.message!!.isNotEmpty()) _ui2State.value = GetOnlineState.Nothing
                    else {
                        _ui2State.value = GetOnlineState.SuccessGetOnlineByUser(r.data)
                    }
                }
        }
    }
}