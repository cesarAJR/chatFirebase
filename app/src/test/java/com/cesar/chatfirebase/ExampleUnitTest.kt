package com.cesar.chatfirebase

import com.cesar.domain.model.User
import com.cesar.domain.repository.IRegisterRepository
import com.cesar.domain.useCase.editUser.EditUserCase
import com.example.domain.core.Result
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleUnitTest {

    @Mock
    private lateinit var repository :IRegisterRepository

    private lateinit var editUserCase : EditUserCase



    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        editUserCase = EditUserCase(repository)
    }

    @Test
    fun edit_user(){
        runBlocking {
            val flow = flow {
                emit(Result.Successfull(""))
            }
            val user  = User()
            `when`(repository.editUser(null,user)).thenReturn(flow)

            val response = editUserCase.execute(null,user)

            var data :String?=null
            var message :String?=null
            response.collect{
                assert(it.data=="")
            }
        }
    }
}