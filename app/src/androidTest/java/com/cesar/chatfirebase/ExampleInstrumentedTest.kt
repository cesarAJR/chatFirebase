package com.cesar.chatfirebase

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cesar.data.datasource.remote.register.IRegisterRemoteDataSource
import com.cesar.data.datasource.remote.register.RegisterRemoteDataSource
import com.cesar.data.repository.RegisterRepository
import com.cesar.domain.repository.IRegisterRepository
import com.cesar.domain.useCase.register.IRegisterUserCase
import com.cesar.domain.useCase.register.RegisterUserCase
import com.example.data.di.provideFirebase
import com.example.data.di.provideFirebaseBD
import com.example.data.di.provideFirebaseStorage
import com.example.data.di.provideSharePreferences

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Before
    fun init(){
        loadKoinModules(
            moduleTest
        )
    }

    @Test
    fun useAppContext() {

    }
}