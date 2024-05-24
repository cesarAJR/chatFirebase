package com.cesar.chatfirebase

import android.app.Application
import com.cesar.chatfirebase.di.appModule
import com.example.data.di.dataModule
import com.example.data.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext

class MyApp:Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(dataModules+ domainModules+ appModules)
        }
    }

}

val appModules = listOf(appModule)
val domainModules = listOf(domainModule)
val dataModules = listOf(dataModule)