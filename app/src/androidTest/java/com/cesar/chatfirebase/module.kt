package com.cesar.chatfirebase


import android.content.Context
import android.content.SharedPreferences
import com.cesar.data.datasource.remote.register.IRegisterRemoteDataSource
import com.cesar.data.datasource.remote.register.RegisterRemoteDataSource
import com.cesar.data.repository.RegisterRepository
import com.cesar.domain.repository.IRegisterRepository
import com.cesar.domain.useCase.register.IRegisterUserCase
import com.cesar.domain.useCase.register.RegisterUserCase
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val moduleTest = module {
    single { provideSharePreferences(androidContext())  }
    single { provideFirebase() }
    single { provideFirebaseBD() }
    single { provideFirebaseStorage() }
    factory<IRegisterRemoteDataSource> { RegisterRemoteDataSource(get(),get(),get(),get()) }
    factory<IRegisterRepository> { RegisterRepository(get()) }
    factory <IRegisterUserCase>{ RegisterUserCase(get()) }
}

fun provideFirebaseStorage(): FirebaseStorage {
    return Firebase.storage
}

fun provideFirebase(): FirebaseAuth {
    return Firebase.auth
}
fun provideFirebaseBD(): FirebaseFirestore {
    return Firebase.firestore
}
fun provideSharePreferences(context : Context) : SharedPreferences {
    return context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
}