package com.cesar.chatfirebase.di

import android.content.Context
import android.content.SharedPreferences
import com.cesar.chatfirebase.viewModel.ChatViewModel
import com.cesar.chatfirebase.viewModel.EditUserViewModel
import com.cesar.chatfirebase.viewModel.LoginViewModel
import com.cesar.chatfirebase.viewModel.RegisterViewModel
import com.cesar.chatfirebase.viewModel.UserListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { LoginViewModel(get(),get()) }
    viewModel { RegisterViewModel(get(),get()) }
    viewModel { UserListViewModel(get(),get()) }
    viewModel { ChatViewModel(get(),get(),get(),get()) }
    viewModel { EditUserViewModel(get()) }
}

