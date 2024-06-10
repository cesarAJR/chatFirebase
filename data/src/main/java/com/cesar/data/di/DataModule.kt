package com.example.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.cesar.data.database.AppDatabase
import com.cesar.data.database.dao.MessageDataDao
import com.cesar.data.database.dao.UserChatDao
import com.cesar.data.datasource.local.chat.ChatLocalDataSource
import com.cesar.data.datasource.local.chat.IChatLocalDataSource
import com.cesar.data.datasource.local.userList.IUserListLocalDataSource
import com.cesar.data.datasource.local.userList.UserListLocalDataSource
import com.cesar.data.datasource.remote.chat.ChatRemoteDataSource
import com.cesar.data.datasource.remote.chat.IChatRemoteDataSource
import com.cesar.data.datasource.remote.login.ILoginRemoteDataSource
import com.cesar.data.datasource.remote.login.LoginRemoteDataSource
import com.cesar.data.datasource.remote.register.IRegisterRemoteDataSource
import com.cesar.data.datasource.remote.register.RegisterRemoteDataSource
import com.cesar.data.datasource.remote.userList.IUserListRemoteDataSource
import com.cesar.data.datasource.remote.userList.UserListRemoteDataSource
import com.cesar.data.repository.ChatRepository
import com.cesar.data.repository.RegisterRepository
import com.cesar.data.repository.UserListRepository
import com.cesar.data.repository.UserRepository
import com.cesar.domain.repository.IChatRepository
import com.cesar.domain.repository.IRegisterRepository
import com.cesar.domain.repository.IUserListRepository
import com.cesar.domain.repository.IUserRepository

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.LocalCacheSettings
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.firestore.persistentCacheSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { provideSharePreferences(androidContext())  }
    single { provideFirebase()}
    single { provideFirebaseBD()}
    single { provideFirebaseStorage()}
    single { provideDatabase(androidContext()) }
    single { provideUserChatDao(get())  }
    single { provideMessageDataDao(get())  }

    factory<ILoginRemoteDataSource> { LoginRemoteDataSource(get()) }
    factory<IRegisterRemoteDataSource> { RegisterRemoteDataSource(get(),get(),get(),get()) }
    factory<IUserListRemoteDataSource> { UserListRemoteDataSource(get(),get()) }
    factory<IChatRemoteDataSource> { ChatRemoteDataSource(get(),get()) }

    factory<IChatLocalDataSource> { ChatLocalDataSource(get(),get(),get()) }
    factory<IUserListLocalDataSource> { UserListLocalDataSource(get(),get()) }

    factory<IUserRepository> { UserRepository(get(),get(),get(),androidContext()) }
    factory<IRegisterRepository> { RegisterRepository(get(),androidContext()) }
    factory<IUserListRepository> { UserListRepository(get(),get(),get()) }
    factory<IChatRepository> { ChatRepository(get(),get(),androidContext()) }
}

fun provideFirebaseStorage():FirebaseStorage{
    return Firebase.storage
}

fun provideFirebase():FirebaseAuth{
    return Firebase.auth
}
fun provideFirebaseBD(): FirebaseFirestore {
    val db = Firebase.firestore
    val settings = firestoreSettings {
        setLocalCacheSettings(memoryCacheSettings {})
    }
    db.firestoreSettings = settings
    return db
}
fun provideSharePreferences(context : Context) : SharedPreferences {
    return context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
}

fun provideDatabase(context : Context) : AppDatabase = Room.databaseBuilder(
    context,
    AppDatabase::class.java,
    "dbChatFirebase"
).build()

fun provideUserChatDao(appDatabase: AppDatabase) : UserChatDao = appDatabase.UserChatDao()
fun provideMessageDataDao(appDatabase: AppDatabase) : MessageDataDao = appDatabase.MessageDataDao()

