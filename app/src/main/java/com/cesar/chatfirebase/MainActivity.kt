package com.cesar.chatfirebase

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.cesar.chatfirebase.presentation.navigation.SetupNavGraph
import com.cesar.chatfirebase.ui.theme.ChatFirebaseTheme
import com.cesar.chatfirebase.viewModel.EditUserViewModel
import com.cesar.chatfirebase.viewModel.LoginViewModel
import com.cesar.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import org.koin.android.ext.android.inject


class MainActivity : ComponentActivity() {

    val firebaseAuth: FirebaseAuth by inject()
    private val preferences: SharedPreferences by inject()
    private val viewModel: EditUserViewModel by inject()

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
