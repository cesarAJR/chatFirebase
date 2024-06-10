package com.cesar.chatfirebase

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Observer
import androidx.navigation.compose.rememberNavController
import com.cesar.chatfirebase.presentation.navigation.SetupNavGraph
import com.cesar.chatfirebase.ui.theme.ChatFirebaseTheme
import com.cesar.chatfirebase.viewModel.EditUserViewModel
import com.cesar.data.system.ConnectionState
import com.cesar.domain.model.User
import com.cesar.domain.repository.IChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext


class MainActivity : ComponentActivity() {

    val firebaseAuth: FirebaseAuth by inject()
    private val preferences: SharedPreferences by inject()
    private val viewModel: EditUserViewModel by inject()
    private val repository: IChatRepository by inject()
    @OptIn(ExperimentalCoroutinesApi::class)
    private val coroutineContext: CoroutineContext = newSingleThreadContext("back") // for example
    private val scope = CoroutineScope(coroutineContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatFirebaseTheme {
                val navController = rememberNavController()
                val isLogged =  firebaseAuth.currentUser!=null
                SetupNavGraph(navController = navController,isLogged =  isLogged)
            }
        }
    }

    fun getUser():User?{
        if (preferences.getString("USER","")?.isNotEmpty() == true){
            return Gson().fromJson(preferences.getString("USER",""), User::class.java)
        }else{
            return null
        }
    }


    fun softInputUnspecified(){
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
    fun softInputResize(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false)
            getWindow().getDecorView().setOnApplyWindowInsetsListener { v, insets ->
                val height: Int = insets.getInsets(WindowInsets.Type.ime()).bottom
                v.setPadding(0, 0, 0, height)
                insets
            }
        }else{
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    override fun onResume() {
        super.onResume()
        updateOnline(true)
        val connectionLiveData = ConnectionState(this@MainActivity)
        connectionLiveData.observe(this, Observer {
           if (it){
               val user = Gson().fromJson(preferences.getString("USER",""), User::class.java)
               if (user!=null){
                   scope.launch(Dispatchers.IO) {
                       val messagesPending =  repository.getListPendingMessage(user.id?:"")
                       if (messagesPending.isNotEmpty()){
                               repository.sendPendingMessage(messagesPending).collect{ result->
                               }
                       }
                   }
               }
           }
        })
    }

    override fun onStop() {
        super.onStop()
        updateOnline(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        updateOnline(false)
    }

     fun updateOnline(online:Boolean){
        if (getUser()!=null){
            val user = getUser()
            user!!.online=online
            viewModel.editOnline(user)
        }
    }
}
