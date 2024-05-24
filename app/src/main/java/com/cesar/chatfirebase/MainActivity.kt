package com.cesar.chatfirebase

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.cesar.chatfirebase.ui.navigation.SetupNavGraph
import com.cesar.chatfirebase.ui.theme.ChatFirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject


class MainActivity : ComponentActivity() {

    val firebaseAuth: FirebaseAuth by inject()

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


    fun softInputUnspecified(){
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
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
}
