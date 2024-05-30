package com.example.data.di

import android.content.Context
import android.content.SharedPreferences
import com.cesar.data.datasource.remote.chat.IChatRemoteDataSource
import com.cesar.data.datasource.remote.userList.IUserListRemoteDataSource
import com.cesar.data.datasource.remote.userList.UserListRemoteDataSource
import com.cesar.data.repository.ChatRepository
import com.cesar.data.repository.UserListRepository
import com.cesar.domain.repository.IChatRepository
import com.cesar.domain.repository.IRegisterRepository
import com.cesar.domain.repository.IUserListRepository
import com.cesar.domain.repository.IUserRepository
import com.example.data.datasource.remote.login.ChatRemoteDataSource
import com.example.data.datasource.remote.login.ILoginRemoteDataSource
import com.example.data.datasource.remote.login.IRegisterRemoteDataSource
import com.example.data.datasource.remote.login.LoginRemoteDataSource
import com.example.data.datasource.remote.login.RegisterRemoteDataSource
import com.example.prueba_softtek.data.repository.RegisterRepository
import com.example.prueba_softtek.data.repository.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { provideSharePreferences(androidContext())  }
    single { provideFirebase()}
    single { provideFirebaseBD()}
    single { provideFirebaseStorage()}
    factory<ILoginRemoteDataSource> { LoginRemoteDataSource(get()) }
    factory<IRegisterRemoteDataSource> { RegisterRemoteDataSource(get(),get(),get(),get()) }
    factory<IUserListRemoteDataSource> { UserListRemoteDataSource(get(),get()) }
    factory<IChatRemoteDataSource> { ChatRemoteDataSource(get(),get()) }
    factory<IUserRepository> { UserRepository(get(),get(),get()) }
    factory<IRegisterRepository> { RegisterRepository(get()) }
    factory<IUserListRepository> { UserListRepository(get()) }
    factory<IChatRepository> { ChatRepository(get()) }
}

fun provideFirebaseStorage():FirebaseStorage{
    return Firebase.storage
}

fun provideFirebase():FirebaseAuth{
    return Firebase.auth
}
fun provideFirebaseBD(): FirebaseFirestore {
    return Firebase.firestore
}
fun provideSharePreferences(context : Context) : SharedPreferences {
    return context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
}

