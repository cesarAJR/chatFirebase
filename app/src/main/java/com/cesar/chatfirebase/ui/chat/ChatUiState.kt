package com.cesar.chatfirebase.ui.chat

import com.cesar.domain.model.Message
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

sealed class ChatUiState {
    data class Success(val message: String?): ChatUiState()
    data class SuccessGetMessage(val messageData: Message?): ChatUiState()
    data class SuccessGetListMessage(val listMessage: MutableList<Message>?): ChatUiState()
    data class Error(val message: String): ChatUiState()
    object Loading: ChatUiState()
    object Nothing: ChatUiState()

}