package com.cesar.chatfirebase

import com.cesar.domain.model.User
import com.cesar.domain.repository.IRegisterRepository
import com.cesar.domain.useCase.editUser.EditUserCase
import com.example.domain.core.Result
import com.cesar.data.repository.RegisterRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.koin.test.inject
import org.mockito.Mockito

class Example2 :KoinTest{

//   private val repository : RegisterRepository by inject()

    private lateinit var editUserCase : EditUserCase

    @Before
    fun init(){
//        startKoin {
//            modules(
//                dataModules+ domainModules +appModules
//            )
//        }

//        editUserCase = EditUserCase(repository)
    }

    @Test
    fun `check MVP hierarchy`() {
        checkModules {
            modules(  dataModules+ domainModules +appModules)
        }
    }

    @Test
    fun koin_text(){
        runBlocking {
            val flow = flow {
                emit(Result.Successfull(""))
            }
            val user  = User()
//            Mockito.`when`(repository.editUser(null,user)).thenReturn(flow)

            val response = editUserCase.execute(null,user)

            var data :String?=null
            var message :String?=null
            response.collect{
                assert(it.data=="")
            }
        }
    }
}