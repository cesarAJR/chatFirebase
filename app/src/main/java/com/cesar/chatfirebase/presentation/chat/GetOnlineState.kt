package com.cesar.chatfirebase.presentation.chat

sealed class GetOnlineState {
    data class SuccessGetOnlineByUser(val online: String?): GetOnlineState()
    data object Nothing: GetOnlineState()
}